package pl.kossa.myflightsserver.restcontrollers

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.data.responses.SharedFlightJoinDetails
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedUserData
import pl.kossa.myflightsserver.exceptions.*
import pl.kossa.myflightsserver.extensions.plusMinutes
import pl.kossa.myflightsserver.services.FirebaseMessagingService
import pl.kossa.myflightsserver.services.FlightsService
import pl.kossa.myflightsserver.services.SharedFlightsService
import java.util.*

@RestController
@RequestMapping("/api/shared-flights")
class SharedFlightsRestController : BaseRestController() {

    @Autowired
    private lateinit var service: SharedFlightsService

    @Autowired
    private lateinit var flightsService: FlightsService

    @Autowired
    private lateinit var firebaseMessagingService: FirebaseMessagingService

    @Autowired
    private lateinit var threadPoolTaskScheduler: ThreadPoolTaskScheduler

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharedFlights(locale: Locale): List<SharedFlight> {
        val user = getUserDetails(locale)
        return service.getSharedFlightsByOwnerId(user.uid)
    }

    @GetMapping("/pending", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getPendingSharedFlights(locale: Locale): List<SharedFlightResponse> {
        val user = getUserDetails(locale)
        val pendingSharedFlights = service.getPendingSharedFlights(user.uid)
        val pendingJoinRequests = service.getPendingJoinRequests(user.uid)
        val allSharedFlights = pendingSharedFlights + pendingJoinRequests
        return allSharedFlights.mapNotNull {
            val sharedFlightUser = it.sharedUserId?.let { sharedUserId -> usersService.getUserById(sharedUserId) }
            val sharedUserData = sharedFlightUser?.let {
                SharedUserData(
                    sharedFlightUser.userId,
                    sharedFlightUser.email,
                    sharedFlightUser.nick,
                    sharedFlightUser.avatar
                )
            }
            val flight = flightsService.getFlightById(it.ownerId, it.flightId)
            val ownerUser = usersService.getUserById(it.ownerId) ?: return@mapNotNull null
            val ownerData = SharedUserData(
                ownerUser.userId,
                ownerUser.email,
                ownerUser.nick,
                ownerUser.avatar
            )
            flight?.let { f ->
                SharedFlightResponse(
                    it.sharedFlightId,
                    f,
                    it.ownerId,
                    ownerData,
                    sharedUserData
                )
            }
        }
    }

    @GetMapping("/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharedFlight(
        @PathVariable("sharedFlightId") sharedFlightId: String,
        locale: Locale
    ): SharedFlightResponse {
        val user = getUserDetails(locale)
        val sharedFlight =
            service.getSharedFlightByOwnerIdAndSharedFlightId(user.uid, sharedFlightId) ?: throw NotFoundException(
                "Shared flight with id '$sharedFlightId' not found"
            )
        val sharedUser = sharedFlight.sharedUserId?.let { usersService.getUserById(it) }
        val sharedUserData = sharedUser?.let {
            SharedUserData(
                it.userId,
                it.email,
                it.nick,
                it.avatar
            )
        }
        val flight = flightsService.getFlightById(sharedFlight.ownerId, sharedFlight.flightId)
            ?: throw NotFoundException("Flight with id '${sharedFlight.flightId}' not found.")
        val owner = usersService.getUserById(sharedFlight.ownerId)
            ?: throw NotFoundException("Owner not found")
        val ownerData = SharedUserData(
            owner.userId,
            owner.email,
            owner.nick,
            owner.avatar
        )
        return SharedFlightResponse(
            sharedFlightId,
            flight,
            sharedFlight.ownerId,
            ownerData,
            sharedUserData
        )
    }


    @PostMapping("/share/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun postSharedFlight(@PathVariable("flightId") flightId: String, locale: Locale): SharedFlight {
        val user = getUserDetails(locale)
        flightsService.getFlightById(user.uid, flightId)
            ?: throw NotFoundException("Flight with id '$flightId' not found.")
        val sharedFlight = service.getSharedFlightByFlightId(user.uid, flightId)
        sharedFlight?.let {
            if (Date() < it.expiresAt) {
                return sharedFlight
            } else {
                service.deleteSharedFlightById(sharedFlight.sharedFlightId)
            }
        }
        val expiresAt = Date().plusMinutes(1)
        val newSharedFlight = SharedFlight(
            UUID.randomUUID().toString(),
            flightId,
            user.uid,
            null,
            false,
            expiresAt,
        )
        val deleteTask = Runnable {
            runBlocking {
                val sf = service.getSharedFlightByFlightId(user.uid, flightId) ?: return@runBlocking
                if (sf.sharedUserId == null) {
                    logger.info("Deleting not joined sharedFlight")
                    service.deleteSharedFlightById(sf.sharedFlightId)
                }
            }
        }
        threadPoolTaskScheduler.schedule(deleteTask, expiresAt)
        return service.save(newSharedFlight)
    }

    @PutMapping("/confirm/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun postSharedFlightConfirmation(@PathVariable("sharedFlightId") sharedFlightId: String, locale: Locale) {
        val user = getUserDetails(locale)
        val sharedFlight =
            service.getSharedFlightByOwnerIdAndSharedFlightId(user.uid, sharedFlightId) ?: throw NotFoundException(
                "Shared flight with id '$sharedFlightId' not found"
            )
        sharedFlight.sharedUserId ?: throw UserNotJoinedException(sharedFlightId)
        if (sharedFlight.isConfirmed) {
            throw AlreadyConfirmedException(sharedFlightId)
        }
        val sharedUser =
            usersService.getUserById(sharedFlight.sharedUserId) ?: throw NotFoundException("Shared user not found")
        firebaseMessagingService.sendSharedFlightConfirmationMessage(
            sharedFlight.sharedUserId,
            sharedFlight.flightId,
            sharedUser.language
        )
        service.save(sharedFlight.copy(isConfirmed = true))
    }

    @GetMapping("/join/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharedFlightBeforeJoin(
        @PathVariable("sharedFlightId") sharedFlightId: String,
        locale: Locale
    ): SharedFlightJoinDetails {
        val user = getUserDetails(locale)
        val sharedFlight = service.getSharedFlightBySharedFlightId(sharedFlightId) ?: throw NotFoundException(
            "Shared flight with id '$sharedFlightId' not found"
        )
        if (sharedFlight.expiresAt < Date()) {
            service.deleteSharedFlightById(sharedFlightId)
            throw NotFoundException(
                "Shared flight with id '$sharedFlightId' not found"
            )
        }
        service.getSharedFlightsBySharedUserIdAndFlightId(user.uid, sharedFlight.flightId)?.let {
            throw FlightAlreadySharedException(sharedFlight.flightId)
        }
        if (sharedFlight.ownerId == user.uid) {
            throw JoiningOwnFlightException()
        }
        if (sharedFlight.isConfirmed) {
            throw AlreadyConfirmedException(sharedFlightId)
        }
        sharedFlight.sharedUserId?.let {
            throw AlreadyJoinedException(sharedFlightId)
        }
        val flight = flightsService.getFlightById(sharedFlight.ownerId, sharedFlight.flightId)
            ?: throw NotFoundException("Flight with id '${sharedFlight.flightId}' not found")
        val ownerUser =
            usersService.getUserById(sharedFlight.ownerId) ?: throw NotFoundException("Flight owner not found")

        return SharedFlightJoinDetails(
            flight,
            SharedUserData(ownerUser.userId, ownerUser.email, ownerUser.nick, ownerUser.avatar)
        )
    }

    @PutMapping("/join/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun postSharedFlightJoin(@PathVariable("sharedFlightId") sharedFlightId: String, locale: Locale) {
        val user = getUserDetails(locale)
        logger.info("Joining flight: ${user.email}")
        val sharedFlight = service.getSharedFlightBySharedFlightId(sharedFlightId) ?: throw NotFoundException(
            "Shared flight with id '$sharedFlightId' not found"
        )
        val owner = usersService.getUserById(sharedFlight.ownerId)
            ?: throw NotFoundException("Shared flight with id '$sharedFlightId' not found")
        if (sharedFlight.expiresAt < Date()) {
            service.deleteSharedFlightById(sharedFlightId)
            throw NotFoundException(
                "Shared flight with id '$sharedFlightId' not found"
            )
        }
        service.getSharedFlightsBySharedUserIdAndFlightId(user.uid, sharedFlight.flightId)?.let {
            throw FlightAlreadySharedException(sharedFlight.flightId)
        }
        if (sharedFlight.ownerId == user.uid) {
            throw JoiningOwnFlightException()
        }
        if (sharedFlight.isConfirmed) {
            throw AlreadyConfirmedException(sharedFlightId)
        }
        sharedFlight.sharedUserId?.let {
            throw AlreadyJoinedException(sharedFlightId)
        }
        val userName = if (user.nick.isNullOrBlank()) user.email else user.nick
        firebaseMessagingService.sendUserSendJoinRequestNotification(
            sharedFlight,
            userName,
            sharedFlightId,
            owner.language
        )
        service.save(sharedFlight.copy(sharedUserId = user.uid))
    }

    @DeleteMapping("/{sharedFlightId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteSharedFlightById(@PathVariable("sharedFlightId") sharedFlightId: String, locale: Locale) {
        val user = getUserDetails(locale)
        service.getSharedFlightByOwnerIdAndSharedFlightId(user.uid, sharedFlightId)
            ?: throw NotFoundException("Shared flight with id '$sharedFlightId' not found")
        service.deleteSharedFlightById(sharedFlightId)
    }

    @DeleteMapping("/resign/{sharedFlightId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun resignFromSharedFlight(@PathVariable("sharedFlightId") sharedFlightId: String, locale: Locale) {
        val user = getUserDetails(locale)
        service.getSharedFlightBySharedUserIdAndSharedFlightId(user.uid, sharedFlightId)
            ?: throw NotFoundException("Shared flight with id '$sharedFlightId' not found")
        service.deleteSharedFlightById(sharedFlightId)
    }
}