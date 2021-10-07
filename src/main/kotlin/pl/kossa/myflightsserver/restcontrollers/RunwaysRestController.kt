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
import pl.kossa.myflightsserver.data.models.Runway
import pl.kossa.myflightsserver.data.requests.RunwayRequest
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.services.AirportsService
import pl.kossa.myflightsserver.services.RunwaysService
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/airports/{airportId}/runways")
class RunwaysRestController : BaseRestController() {

    @Autowired
    private lateinit var runwaysService: RunwaysService

    @Autowired
    private lateinit var airportsService: AirportsService

    @GetMapping("/{runwayId}", produces = [MediaType.APPLICATION_JSON_VALUE])
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
    suspend fun getRunwayById(
        @PathVariable("runwayId") runwayId: String,
    ): Runway {
        val user = getUserDetails()
        return runwaysService.getRunwayById(user.uid, runwayId)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
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
    suspend fun postRunway(
        @PathVariable("airportId") airportId: String,
        @RequestBody @Valid runwayRequest: RunwayRequest
    ): CreatedResponse {
        val user = getUserDetails()
        val airport = airportsService.getAirportById(user.uid, airportId)
        val runway = runwayRequest.toRunway(UUID.randomUUID().toString(), user.uid)
        runwaysService.saveRunway(runway)
        airport.runways.add(runway)
        airportsService.saveAirport(airport)
        return CreatedResponse(runway.runwayId)
    }

    @PutMapping(
        "/{runwayId}",
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
    suspend fun putRunway(
        @PathVariable("airportId") airportId: String,
        @PathVariable("runwayId") runwayId: String,
        @RequestBody @Valid runwayRequest: RunwayRequest
    ) {
        val user = getUserDetails()
        val airport = airportsService.getAirportById(user.uid, airportId)
        airport.runways.find { it.runwayId == runwayId }
            ?: throw NotFoundException("Runway with id '$runwayId' not found.")
        val newRunway = runwayRequest.toRunway(runwayId, user.uid)
        runwaysService.saveRunway(newRunway)
    }

    @DeleteMapping("/{runwayId}", produces = [MediaType.APPLICATION_JSON_VALUE])
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
    suspend fun deleteRunway(
        @PathVariable("airportId") airportId: String, @PathVariable("runwayId") runwayId: String
    ) {
        val user = getUserDetails()
        val airport = airportsService.getAirportById(user.uid, airportId)
        airport.runways.find { it.runwayId == runwayId }
            ?: throw NotFoundException("Runway with id '$runwayId' not found.")
        airport.runways.removeIf { it.runwayId == runwayId }
        airportsService.saveAirport(airport)
        runwaysService.deleteRunwayById(runwayId)
    }

}
