package pl.kossa.myflightsserver.restcontrollers

//import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.User

@RestController
@RequestMapping("/api/user")
class UsersRestController : BaseRestController() {


    //    @Operation(summary = "Get user data")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUser(): ResponseEntity<User> {
        return ResponseEntity.ok(securityService.getUser() ?: throw Exception())
    }
}