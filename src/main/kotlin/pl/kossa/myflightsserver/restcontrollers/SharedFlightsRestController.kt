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
import pl.kossa.myflightsserver.services.FlightsService
import pl.kossa.myflightsserver.services.SharedFlightsService
import java.util.*

@RequestMapping("/api/shared-flights")
class SharedFlightsRestController : BaseRestController() {

    @Autowired
    private lateinit var service: SharedFlightsService

    @Autowired
    private lateinit var flightsService: FlightsService


    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharedFlights(): List<SharedFlight> {
        val user = getUserDetails()
        return service.getSharedFlightsByUserId(user.uid)
    }

    @GetMapping("/{sharedFlightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharedFlight(@PathVariable("sharedFlightId") sharedFlightId: String): SharedFlightResponse {
        val user = getUserDetails()
        val sharedFlight =
            service.getSharedFlightByUserIdAndSharedFlightId(user.uid, sharedFlightId) ?: throw NotFoundException(
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
        return SharedFlightResponse(
            sharedFlightId,
            sharedFlight.flight,
            sharedFlight.ownerId,
            sharedUserData
        )
    }

    @PostMapping("/share/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun postSharedFlight(@PathVariable("flightId") flightId: String): CreatedResponse {
        val user = getUserDetails()
        val flight = flightsService.getFlightById(user.uid, flightId)
        val sharedFlight = service.getSharedFlightByFlightId(user.uid, flightId)
        sharedFlight?.let {
            return CreatedResponse(it.flightId)
        }
        val newSharedFlight = SharedFlight(
            UUID.randomUUID().toString(),
            flight,
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
            service.getSharedFlightByUserIdAndSharedFlightId(user.uid, sharedFlightId) ?: throw NotFoundException(
                "Shared flight with id '$sharedFlightId' not found"
            )
        sharedFlight.sharedUserId ?: throw  UserNotJoinedException(sharedFlightId)
        if (sharedFlight.isConfirmed) {
            throw AlreadyConfirmedException(sharedFlightId)
        }
        // TODO send notification to shared user
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

    // TODO delete by shared user
    // TODO delete by flightId
    // TODO delete by sharedFlightId

}