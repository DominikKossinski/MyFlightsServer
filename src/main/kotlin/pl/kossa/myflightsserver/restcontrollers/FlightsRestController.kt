package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.UserDetails
import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.data.requests.FlightRequest
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.exceptions.ArrivalTimeException
import pl.kossa.myflightsserver.exceptions.FlightTimeException
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.exceptions.PlannedFlightTimeException
import pl.kossa.myflightsserver.services.AirplanesService
import pl.kossa.myflightsserver.services.AirportsService
import pl.kossa.myflightsserver.services.FlightsService
import java.util.*

@RestController
@RequestMapping("/api/flights")
class FlightsRestController : BaseRestController() {

    @Autowired
    lateinit var flightsService: FlightsService

    @Autowired
    lateinit var airplanesService: AirplanesService

    @Autowired
    lateinit var airportsService: AirportsService


    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            )
        ]
    )
    suspend fun getUserFlights(): List<Flight> {
        val user = getUserDetails()
        return flightsService.getFlightsByUserIdAndPlanned(user.uid, false)
    }

    @GetMapping("/planned", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            )
        ]
    )
    suspend fun getUserPlannedFlights(): List<Flight> {
        val user = getUserDetails()
        return flightsService.getFlightsByUserIdAndPlanned(user.uid, true)
    }

    @GetMapping("/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = NotFoundError::class))]
            )
        ]
    )
    suspend fun getFlightById(@PathVariable("flightId") flightId: String): Flight {
        val user = getUserDetails()
        return flightsService.getFlightById(user.uid, flightId)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = NotFoundError::class))]
            )
        ]
    )
    suspend fun postFlight(@RequestBody flightRequest: FlightRequest): CreatedResponse {
        val user = getUserDetails()
        val flight = validateFlightRequest(flightRequest, user)
        val flightAdded = flightsService.saveFlight(flight)
        return CreatedResponse(flightAdded.flightId)
    }

    @PutMapping(
        "/{flightId}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = NotFoundError::class))]
            )
        ]
    )
    suspend fun putFlight(@PathVariable("flightId") flightId: String, @RequestBody flightRequest: FlightRequest) {
        val user = getUserDetails()
        val flight = flightsService.getFlightById(user.uid, flightId)
        if (flight.image != null && flightRequest.imageId == null) {
            deleteImage(flight.image)
        }
        val updatedFlight = validateFlightRequest(flightRequest, user, flightId)
        flightsService.saveFlight(updatedFlight)
    }

    @DeleteMapping("/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = NotFoundError::class))]
            )
        ]
    )
    suspend fun deleteFlight(@PathVariable("flightId") flightId: String) {
        val user = getUserDetails()
        val flight = flightsService.getFlightById(user.uid, flightId)
        flight.image?.let { deleteImage(it) }
        flightsService.deleteFlightById(flightId)
    }

    private suspend fun validateFlightRequest(
        flightRequest: FlightRequest,
        user: UserDetails,
        flightId: String = UUID.randomUUID().toString()
    ): Flight {
        val image = flightRequest.imageId?.let { imagesService.getImageById(user.uid, it) }


        val airplane = airplanesService.getAirplaneById(user.uid, flightRequest.airplaneId)

        val departureAirport = airportsService.getAirportById(user.uid, flightRequest.departureAirportId)
        val departureRunway = departureAirport.runways.find { it.runwayId == flightRequest.departureRunwayId }
            ?: throw NotFoundException("Runway with id '${flightRequest.departureRunwayId}' not found.")

        val arrivalAirport = airportsService.getAirportById(user.uid, flightRequest.arrivalAirportId)
        val arrivalRunway = arrivalAirport.runways.find { it.runwayId == flightRequest.arrivalRunwayId }
            ?: throw NotFoundException("Runway with id '${flightRequest.arrivalRunwayId}' not found.")
        if (flightRequest.arrivalDate < flightRequest.departureDate) {
            throw ArrivalTimeException()
        }
        if (flightRequest.isPlanned &&
            (flightRequest.departureDate.before(Date()) || flightRequest.arrivalDate.before(Date()))
        ) {
            throw PlannedFlightTimeException()
        }
        if (!flightRequest.isPlanned &&
            (flightRequest.arrivalDate.after(Date()) || flightRequest.departureDate.after(Date()))
        ) {
            throw FlightTimeException()
        }
        return Flight(
            flightId,
            flightRequest.note,
            flightRequest.distance,
            image,
            flightRequest.departureDate,
            flightRequest.arrivalDate,
            user.uid,
            airplane,
            departureAirport,
            departureRunway,
            arrivalAirport,
            arrivalRunway,
            flightRequest.isPlanned
        )
    }
}
