package pl.kossa.myflightsserver.restcontrollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.services.FlightsService
import pl.kossa.myflightsserver.services.SharedFlightsService
import java.util.*

@RequestMapping("/api/share")
class SharedFlightsRestController : BaseRestController() {

    @Autowired
    private lateinit var flightsService: FlightsService

    @Autowired
    private lateinit var service: SharedFlightsService


    @PostMapping("/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
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

}