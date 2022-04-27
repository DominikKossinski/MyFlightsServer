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
import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.data.requests.AirportRequest
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.errors.ExistingEntityType
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.exceptions.ExistingFlightsException
import pl.kossa.myflightsserver.services.AirportsService
import pl.kossa.myflightsserver.services.FlightsService
import pl.kossa.myflightsserver.services.RunwaysService
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/airports")
class AirportsRestController : BaseRestController() {

    @Autowired
    private lateinit var airportsService: AirportsService

    @Autowired
    private lateinit var runwaysService: RunwaysService

    @Autowired
    private lateinit var flightsService: FlightsService


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
    suspend fun getUserAirports(
        @RequestParam(
            name = "filter",
            defaultValue = "",
            required = false
        ) filter: String,
        locale: Locale = Locale.US
    ): List<Airport> {
        val user = getUserDetails(locale)
        return airportsService.getAirportsByUserId(user.uid, filter)
    }

    @GetMapping("/{airportId}", produces = [MediaType.APPLICATION_JSON_VALUE])
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
    suspend fun getAirportById(@PathVariable("airportId") airportId: String, locale: Locale = Locale.US): Airport {
        val user = getUserDetails(locale)
        return airportsService.getAirportById(user.uid, airportId)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
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
            )
        ]
    )
    suspend fun postAirport(
        @RequestBody @Valid airportRequest: AirportRequest,
        locale: Locale = Locale.US
    ): CreatedResponse {
        val user = getUserDetails(locale)
        val image = airportRequest.imageId?.let { imagesService.getImageById(user.uid, it) }
        val airport =
            airportsService.saveAirport(airportRequest.toAirport(UUID.randomUUID().toString(), image, user.uid))
        return CreatedResponse(airport.airportId)
    }

    @PutMapping(
        "/{airportId}",
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
    suspend fun putAirport(
        @PathVariable("airportId") airportId: String,
        @RequestBody @Valid airportRequest: AirportRequest,
        locale: Locale = Locale.US
    ) {
        val user = getUserDetails(locale)
        val airport = airportsService.getAirportById(user.uid, airportId)
        if (airport.image != null && airportRequest.imageId == null) {
            deleteImage(airport.image)
        }
        val image = airportRequest.imageId?.let { imagesService.getImageById(user.uid, it) }
        airportsService.saveAirport(airportRequest.toAirport(airportId, image, user.uid))
    }

    @DeleteMapping("/{airportId}", produces = [MediaType.APPLICATION_JSON_VALUE])
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
    suspend fun deleteAirport(@PathVariable("airportId") airportId: String, locale: Locale = Locale.US) {
        val user = getUserDetails(locale)
        val airport = airportsService.getAirportById(user.uid, airportId)
        val flights = flightsService.getFlightsByUserId(user.uid).filter {
            it.departureAirport.airportId == airportId || it.arrivalAirport.airportId == airportId
        }
        if (flights.isNotEmpty()) {
            throw ExistingFlightsException(ExistingEntityType.AIRPORT, airportId)
        }
        airport.image?.let { deleteImage(it) }
        for (runway in airport.runways) {
            runwaysService.deleteRunwayById(runway.runwayId)
        }
        airportsService.deleteAirportById(airportId)
    }

}
