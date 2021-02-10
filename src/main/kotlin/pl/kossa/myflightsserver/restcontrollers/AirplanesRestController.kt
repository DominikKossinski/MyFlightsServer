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
import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.data.requests.AirplaneRequest
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.services.AirplanesService
import javax.validation.Valid

@RestController
@RequestMapping("/api/airplanes")
class AirplanesRestController : BaseRestController() {

    @Autowired
    lateinit var airplanesService: AirplanesService


    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))])
    ])
    fun getUserAirplanes(): ResponseEntity<List<Airplane>> {
        val user = getUserDetails()
        return ResponseEntity.ok(airplanesService.getAirplanesByUserId(user.uid))
    }

    @GetMapping("/{airplaneId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun getAirplaneById(@PathVariable("airplaneId") airplaneId: Int): ResponseEntity<Airplane> {
        val user = getUserDetails()
        val airplane = airplanesService.getAirplaneById(airplaneId, user.uid)
        return ResponseEntity.ok(airplane)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))])
    ])
    fun postAirplane(@RequestBody @Valid airplaneRequest: AirplaneRequest): ResponseEntity<CreatedResponse> {
        val user = getUserDetails()
        val airplane = Airplane(
            0,
            airplaneRequest.name,
            airplaneRequest.maxSpeed,
            airplaneRequest.weight,
            airplaneRequest.image,
            user.uid
        )
        val airplaneAdded = airplanesService.saveAirplane(airplane)
        return ResponseEntity.status(HttpStatus.CREATED).body(CreatedResponse(airplaneAdded.airplaneId))
    }

    @PutMapping("/{airplaneId}", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun putAirplane(@PathVariable("airplaneId") airplaneId: Int, @RequestBody @Valid airplaneRequest: AirplaneRequest) {
        val user = getUserDetails()
        airplanesService.getAirplaneById(airplaneId, user.uid)
        val airplane = Airplane(
            airplaneId,
            airplaneRequest.name,
            airplaneRequest.maxSpeed,
            airplaneRequest.weight,
            airplaneRequest.image,
            user.uid
        )
        airplanesService.saveAirplane(airplane)
    }

    @DeleteMapping("/{airplaneId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "204"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = NotFoundError::class))])
    ])
    fun deleteAirplane(@PathVariable("airplaneId") airplaneId: Int) {
        val user = getUserDetails()
        airplanesService.getAirplaneById(airplaneId, user.uid)
        airplanesService.deleteAirplaneById(airplaneId)
    }

}