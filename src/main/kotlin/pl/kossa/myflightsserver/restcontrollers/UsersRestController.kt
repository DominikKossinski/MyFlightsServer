package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.UserDetails
import pl.kossa.myflightsserver.data.models.User
import pl.kossa.myflightsserver.data.requests.UserRequest
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.UnauthorizedError

@RestController
@RequestMapping("/api/user")
class UsersRestController : BaseRestController() {


    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))])
    ])
    fun getUser(): ResponseEntity<UserDetails> {
        return ResponseEntity.ok(getUserDetails())
    }


    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = [
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = UnauthorizedError::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content(schema = Schema(implementation = ForbiddenError::class))])
    ])
    fun putUser(@RequestBody userRequest: UserRequest) {
        val user = getUserDetails()
        val updatedUser = User(user.uid, userRequest.nick, user.email, userRequest.image)
        usersService.saveUser(updatedUser)
    }

}