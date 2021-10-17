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
import pl.kossa.myflightsserver.data.models.User
import pl.kossa.myflightsserver.data.requests.UserRequest
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.services.AirplanesService
import pl.kossa.myflightsserver.services.AirportsService
import pl.kossa.myflightsserver.services.FlightsService
import pl.kossa.myflightsserver.services.RunwaysService

@RestController
@RequestMapping("/api/user")
class UsersRestController : BaseRestController() {

    @Autowired
    private lateinit var airplanesService: AirplanesService

    @Autowired
    private lateinit var airportsService: AirportsService

    @Autowired
    private lateinit var flightsService: FlightsService

    @Autowired
    private lateinit var runwaysService: RunwaysService

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
    suspend fun getUser(): UserDetails {
        return getUserDetails()
    }


    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
    suspend fun putUser(@RequestBody userRequest: UserRequest) {
        val user = getUserDetails()
        val image = userRequest.imageId?.let { imagesService.getImageById(user.uid, it) }
        val updatedUser = User(user.uid, userRequest.nick, user.email, image)
        usersService.saveUser(updatedUser)
    }

    @DeleteMapping
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
    suspend fun deleteUser() {
        val user = getUserDetails()

        flightsService.deleteAllByUserId(user.uid)
        airplanesService.deleteAllByUserId(user.uid)
        runwaysService.deleteAllByUserId(user.uid)
        airportsService.deleteAllByUserId(user.uid)
        imagesService.deleteAllByUserId(user.uid)

        user.avatar?.let { deleteImage(it) }
        usersService.deleteById(user.uid)
    }

    @DeleteMapping("avatar")
    suspend fun deleteUserAvatar() {
        val user = getUserDetails()
        user.avatar?.let {
            deleteImage(it)
            val userEntity = usersService.getUserById(user.uid)
            userEntity?.let { usersService.saveUser(userEntity.copy(avatar = null)) }
        }
    }
}
