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
import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.data.requests.AirplaneRequest
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.services.AirplanesService
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/airplanes")
class AirplanesRestController : BaseRestController() {

    @Autowired
    lateinit var airplanesService: AirplanesService


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
    suspend fun getUserAirplanes(
        @RequestParam(
            name = "filter",
            defaultValue = "",
            required = false
        ) filter: String
    ): List<Airplane> {
        val user = getUserDetails()
        return airplanesService.getAirplanesByUserId(user.uid, filter.lowercase())
    }

    @GetMapping("/{airplaneId}", produces = [MediaType.APPLICATION_JSON_VALUE])
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
    suspend fun getAirplaneById(@PathVariable("airplaneId") airplaneId: String): Airplane {
        val user = getUserDetails()
        return airplanesService.getAirplaneById(user.uid, airplaneId)
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
            )
        ]
    )
    suspend fun postAirplane(@RequestBody @Valid airplaneRequest: AirplaneRequest): CreatedResponse {
        val user = getUserDetails()
        val airplane = Airplane(
            UUID.randomUUID().toString(),
            airplaneRequest.name,
            airplaneRequest.maxSpeed,
            airplaneRequest.weight,
            airplaneRequest.image,
            user.uid
        )
        val newId = airplanesService.saveAirplane(airplane).airplaneId
        return CreatedResponse(newId)
    }

    @PutMapping(
        "/{airplaneId}",
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
    suspend fun putAirplane(
        @PathVariable("airplaneId") airplaneId: String,
        @RequestBody @Valid airplaneRequest: AirplaneRequest
    ) {
        val user = getUserDetails()
        airplanesService.getAirplaneById(user.uid, airplaneId)
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
    suspend fun deleteAirplane(@PathVariable("airplaneId") airplaneId: String) {
        val user = getUserDetails()
        airplanesService.getAirplaneById(user.uid, airplaneId)
        airplanesService.deleteAirplaneById(airplaneId)
    }

}
