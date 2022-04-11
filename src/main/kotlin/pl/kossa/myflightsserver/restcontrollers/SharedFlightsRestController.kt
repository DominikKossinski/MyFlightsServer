package pl.kossa.myflightsserver.restcontrollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.data.responses.SharedFlightJoinDetails
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedUserData
import pl.kossa.myflightsserver.exceptions.*
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


    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharedFlights(): List<SharedFlight> {
        val user = getUserDetails()
        return service.getSharedFlightsByOwnerId(user.uid)
    }

    @GetMapping("/pending", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getPendingSharedFlights(): List<SharedFlightResponse> {
        val user = getUserDetails()
        val pendingSharedFlights = service.getPendingSharedFlights(user.uid)
        return pendingSharedFlights.mapNotNull {
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
            flight?.let { f ->
                SharedFlightResponse(
                    it.sharedFlightId,
                    f,
                    it.ownerId,
                    sharedUserData
                )
            }
        }
    }

    @GetMapping("/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharedFlight(@PathVariable("sharedFlightId") sharedFlightId: String): SharedFlightResponse {
        val user = getUserDetails()
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
        return SharedFlightResponse(
            sharedFlightId,
            flight,
            sharedFlight.ownerId,
            sharedUserData
        )
    }

    @PostMapping("/share/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun postSharedFlight(@PathVariable("flightId") flightId: String): CreatedResponse {
        val user = getUserDetails()
        flightsService.getFlightById(user.uid, flightId)
            ?: throw NotFoundException("Flight with id '$flightId' not found.")
        val sharedFlight = service.getSharedFlightByFlightId(user.uid, flightId)
        sharedFlight?.let {
            return CreatedResponse(it.sharedFlightId)
        }
        val newSharedFlight = SharedFlight(
            UUID.randomUUID().toString(),
            flightId,
            user.uid,
            null,
            false
        )
        val savedSharedFlight: SharedFlight = service.save(newSharedFlight)
        return CreatedResponse(savedSharedFlight.sharedFlightId)
    }

    @PutMapping("/confirm/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun postSharedFlightConfirmation(@PathVariable("sharedFlightId") sharedFlightId: String) {
        val user = getUserDetails()
        val sharedFlight =
            service.getSharedFlightByOwnerIdAndSharedFlightId(user.uid, sharedFlightId) ?: throw NotFoundException(
                "Shared flight with id '$sharedFlightId' not found"
            )
        sharedFlight.sharedUserId ?: throw  UserNotJoinedException(sharedFlightId)
        if (sharedFlight.isConfirmed) {
            throw AlreadyConfirmedException(sharedFlightId)
        }
        firebaseMessagingService.sendSharedFlightConfirmationMessage(sharedFlight.sharedUserId)
        service.save(sharedFlight.copy(isConfirmed = true))
    }

    @GetMapping("/join/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharedFlightBeforeJoin(@PathVariable("sharedFlightId") sharedFlightId: String): SharedFlightJoinDetails {
        val user = getUserDetails()
        val sharedFlight = service.getSharedFlightBySharedFlightId(sharedFlightId) ?: throw NotFoundException(
            "Shared flight with id '$sharedFlightId' not found"
        )
        service.getSharedFlightsBySharedUserIdAndFlightId(user.uid, sharedFlight.flightId)?.let {
            throw FlightAlreadySharedException(sharedFlight.flightId)
        }
        if (sharedFlight.ownerId == user.uid) {
            throw  JoiningOwnFlightException()
        }
        if (sharedFlight.isConfirmed) {
            throw  AlreadyConfirmedException(sharedFlightId)
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
    suspend fun poshSharedFlightJoin(@PathVariable("sharedFlightId") sharedFlightId: String) {
        val user = getUserDetails()
        logger.info("Joining flight: ${user.email}")
        val sharedFlight = service.getSharedFlightBySharedFlightId(sharedFlightId) ?: throw NotFoundException(
            "Shared flight with id '$sharedFlightId' not found"
        )
        service.getSharedFlightsBySharedUserIdAndFlightId(user.uid, sharedFlight.flightId)?.let {
            throw FlightAlreadySharedException(sharedFlight.flightId)
        }
        if (sharedFlight.ownerId == user.uid) {
            throw  JoiningOwnFlightException()
        }
        if (sharedFlight.isConfirmed) {
            throw  AlreadyConfirmedException(sharedFlightId)
        }
        sharedFlight.sharedUserId?.let {
            throw AlreadyJoinedException(sharedFlightId)
        }
        firebaseMessagingService.sendSharedFlightUserJoinedMessage(sharedFlight)
        service.save(sharedFlight.copy(sharedUserId = user.uid))
    }

    @DeleteMapping("/{sharedFlightId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteSharedFlightById(@PathVariable("sharedFlightId") sharedFlightId: String) {
        val user = getUserDetails()
        service.getSharedFlightByOwnerIdAndSharedFlightId(user.uid, sharedFlightId)
            ?: throw NotFoundException("Shared flight with id '$sharedFlightId' not found")
        service.deleteSharedFlightById(sharedFlightId)
    }

    @DeleteMapping("/resign/{sharedFlightId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun resignFromSharedFlight(@PathVariable("sharedFlightId") sharedFlightId: String) {
        val user = getUserDetails()
        service.getSharedFlightBySharedUserIdAndSharedFlightId(user.uid, sharedFlightId)
            ?: throw NotFoundException("Shared flight with id '$sharedFlightId' not found")
        service.deleteSharedFlightById(sharedFlightId)
    }
}