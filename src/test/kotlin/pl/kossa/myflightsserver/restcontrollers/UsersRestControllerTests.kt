package pl.kossa.myflightsserver.restcontrollers

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
    suspend fun getUser() {
        val user = usersRestController.getUser()
        assert(user.isEmailVerified)
        assert(user.email == "test@test.pl")
        assert(user.nick == "Test")
        assert(user.image == null)
    }

    @Test
    @Order(2)
    suspend fun putUser() {
        usersRestController.putUser(UserRequest("NewNick", null))
        val user = usersRestController.getUser()
        assert(user.isEmailVerified)
        assert(user.email == "test@test.pl")
        assert(user.nick == "NewNick")
        assert(user.image == null)
    }
}
