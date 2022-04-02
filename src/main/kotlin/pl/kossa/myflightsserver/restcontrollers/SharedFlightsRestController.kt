package pl.kossa.myflightsserver.restcontrollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedUserData
import pl.kossa.myflightsserver.exceptions.AlreadyConfirmedException
import pl.kossa.myflightsserver.exceptions.AlreadyJoinedException
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.exceptions.UserNotJoinedException
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
            return CreatedResponse(it.flightId)
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

    @PutMapping("/join/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun poshSharedFlightJoin(@PathVariable("sharedFlightId") sharedFlightId: String) {
        val user = getUserDetails()
        val sharedFlight = service.getSharedFlightBySharedFlightId(sharedFlightId) ?: throw NotFoundException(
            "Shared flight with id '$sharedFlightId' not found"
        )
        if (sharedFlight.isConfirmed) {
            throw  AlreadyConfirmedException(sharedFlightId)
        }
        sharedFlight.sharedUserId?.let {
            throw AlreadyJoinedException(sharedFlightId)
        }
        // TODO send notification to owner
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