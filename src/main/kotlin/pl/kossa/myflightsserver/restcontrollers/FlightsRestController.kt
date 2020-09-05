package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.UserDetails
import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.data.requests.FlightRequest
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.exceptions.ForbiddenException
import pl.kossa.myflightsserver.services.AirplanesService
import pl.kossa.myflightsserver.services.AirportsService
import pl.kossa.myflightsserver.services.FlightsService
import pl.kossa.myflightsserver.services.RunwaysService

@RestController
@RequestMapping("/api/flights")
class FlightsRestController : BaseRestController() {

    @Autowired
    lateinit var flightsService: FlightsService

    @Autowired
    lateinit var airplanesService: AirplanesService

    @Autowired
    lateinit var runwaysService: RunwaysService

    @Autowired
    lateinit var airportsService: AirportsService


    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))])
    ])
    fun getUserFlights(): ResponseEntity<List<Flight>> {
        val user = getUserDetails()
        return ResponseEntity.ok(flightsService.getFlightsByUserId(user.uid))
    }

    @GetMapping("/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun getFlightById(@PathVariable("flightId") flightId: Int): ResponseEntity<Flight> {
        val user = getUserDetails()
        val flight = flightsService.getFlightById(flightId)
        if (flight.userId != user.uid) throw ForbiddenException()
        return ResponseEntity.ok(flight)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = [
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun postFlight(@RequestBody flightRequest: FlightRequest) {
        val user = getUserDetails()
        val flight = validateFlightRequest(flightRequest, user)
        flightsService.saveFlight(flight)
    }

    @PutMapping("/{flightId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun putFLight(@PathVariable("flightId") flightId: Int, @RequestBody flightRequest: FlightRequest) {
        val user = getUserDetails()
        val flight = flightsService.getFlightById(flightId)
        if (flight.userId != user.uid) throw ForbiddenException()
        val updatedFlight = validateFlightRequest(flightRequest, user, flightId)
        flightsService.saveFlight(updatedFlight)
    }

    @DeleteMapping("/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun deleteFlight(@PathVariable("flightId") flightId: Int) {
        flightsService.getFlightById(flightId)
        flightsService.deleteFlightById(flightId)
    }

    private fun validateFlightRequest(flightRequest: FlightRequest, user: UserDetails, flightId: Int = 0): Flight {
        val airplane = airplanesService.getAirplaneById(flightRequest.airplaneId)
        if (airplane.userId != user.uid) throw ForbiddenException()
        val arrivalRunway = runwaysService.getRunwayById(flightRequest.arrivalRunwayId)
        if (arrivalRunway.airport.userId != user.uid) throw ForbiddenException()
        val departureRunway = runwaysService.getRunwayById(flightRequest.departureRunwayId)
        if (departureRunway.airport.userId != user.uid) throw ForbiddenException()
        return Flight(flightId, flightRequest.note, flightRequest.distance, flightRequest.imageUrl,
                flightRequest.startDate, flightRequest.endDate, user.uid, airplane, departureRunway, arrivalRunway)
    }
}