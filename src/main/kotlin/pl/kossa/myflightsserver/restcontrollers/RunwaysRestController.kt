package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
import javax.validation.Valid

@RestController
@RequestMapping("/api/runways")
class RunwaysRestController : BaseRestController() {

    @Autowired
    private lateinit var runwaysService: RunwaysService

    @Autowired
    private lateinit var airportsService: AirportsService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))])
    ])
    fun getRunwaysByAirportId(@Param("airportId") airportId: Int): ResponseEntity<List<Runway>> {
        val user = getUserDetails()
        airportsService.getAirportById(airportId, user.uid)
        return ResponseEntity.ok(runwaysService.getRunwaysByAirportId(airportId))
    }

    @GetMapping("/{runwayId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun getRunwayById(@PathVariable("runwayId") runwayId: Int): ResponseEntity<Runway> {
        val user = getUserDetails()
        val runway = runwaysService.getRunwayById(runwayId)
        if (runway.airport.userId != user.uid) throw NotFoundException("Runway with id '$runwayId' not found.")
        return ResponseEntity.ok(runway)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun postRunway(@RequestBody @Valid runwayRequest: RunwayRequest): ResponseEntity<CreatedResponse> {
        val user = getUserDetails()
        val airport = airportsService.getAirportById(runwayRequest.airportId, user.uid)
        val runway = runwaysService.saveRunway(runwayRequest.toRunway(0, airport))
        return ResponseEntity.status(HttpStatus.CREATED).body(CreatedResponse(runway.runwayId))
    }

    @PutMapping("/{runwayId}", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun putRunway(@PathVariable("runwayId") runwayId: Int, @RequestBody @Valid runwayRequest: RunwayRequest) {
        val user = getUserDetails()
        runwaysService.getRunwayById(runwayId)
        val airport = airportsService.getAirportById(runwayRequest.airportId, user.uid)
        runwaysService.saveRunway(runwayRequest.toRunway(runwayId, airport))
    }

    @DeleteMapping("/{runwayId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun deleteRunway(@PathVariable("runwayId") runwayId: Int) {
        val user = getUserDetails()
        val runway = runwaysService.getRunwayById(runwayId)
        if (runway.airport.userId != user.uid) throw NotFoundException("Runway with id '$runwayId' not found.")
        runwaysService.deleteByRunwayId(runwayId)
    }

}