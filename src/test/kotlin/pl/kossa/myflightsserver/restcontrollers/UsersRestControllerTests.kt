package pl.kossa.myflightsserver.restcontrollers

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import pl.kossa.myflightsserver.config.DataSourceTestConfig
import pl.kossa.myflightsserver.config.FirebaseTestConfig
import pl.kossa.myflightsserver.config.RestControllersTestConfig
import pl.kossa.myflightsserver.data.requests.UserRequest

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UsersRestControllerTests {


    @Autowired
    private lateinit var usersRestController: UsersRestController

    @Test
    @Order(1)
    fun getUser() {
        val response = usersRestController.getUser()
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val user = response.body!!
        assert(user.isEmailVerified)
        assert(user.email == "test@test.pl")
        assert(user.nick == "Test")
        assert(user.imageUrl == null)
    }

    @Test
    @Order(2)
    fun putUser() {
        usersRestController.putUser(UserRequest("NewNick", "Url"))
        val response = usersRestController.getUser()
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val user = response.body!!
        assert(user.isEmailVerified)
        assert(user.email == "test@test.pl")
        assert(user.nick == "NewNick")
        assert(user.imageUrl == "Url")
    }
}