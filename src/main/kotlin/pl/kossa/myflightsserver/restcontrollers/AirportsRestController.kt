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
import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.data.requests.AirportRequest
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.services.AirportsService
import javax.validation.Valid

@RestController
@RequestMapping("/api/airports")
class AirportsRestController : BaseRestController() {

    @Autowired
    private lateinit var airportsService: AirportsService


    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))])
    ])
    fun getUserAirports(): ResponseEntity<List<Airport>> {
        val user = getUserDetails()
        return ResponseEntity.ok(airportsService.getAirportsByUserId(user.uid))
    }

    @GetMapping("/{airportId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun getAirportById(@PathVariable("airportId") airportId: Int): ResponseEntity<Airport> {
        val user = getUserDetails()
        return ResponseEntity.ok(airportsService.getAirportById(airportId, user.uid))
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))])
    ])
    fun postAirport(@RequestBody @Valid airportRequest: AirportRequest): ResponseEntity<CreatedResponse> {
        val user = getUserDetails()
        val airport = airportsService.saveAirport(airportRequest.toAirport(0, user.uid))
        return ResponseEntity.status(HttpStatus.CREATED).body(CreatedResponse(airport.airportId))
    }

    @PutMapping("/{airportId}", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun putAirport(@PathVariable("airportId") airportId: Int, @RequestBody @Valid airportRequest: AirportRequest) {
        val user = getUserDetails()
        airportsService.getAirportById(airportId, user.uid)
        airportsService.saveAirport(airportRequest.toAirport(airportId, user.uid))
    }

    @DeleteMapping("/{airportId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun deleteAirport(@PathVariable("airportId") airportId: Int) {
        val user = getUserDetails()
        airportsService.getAirportById(airportId, user.uid)
        airportsService.deleteAirportById(airportId)
    }

}