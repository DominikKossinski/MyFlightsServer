package pl.kossa.myflightsserver.restcontrollers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.kossa.myflightsserver.config.DataSourceTestConfig
import pl.kossa.myflightsserver.config.FirebaseTestConfig
import pl.kossa.myflightsserver.config.RestControllersTestConfig
import pl.kossa.myflightsserver.data.requests.UserRequest

@ExperimentalCoroutinesApi
@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersRestControllerTests {


    @Autowired
    private lateinit var usersRestController: UsersRestController

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @BeforeAll
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @AfterAll
    fun clear() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }


    @Test
    @Order(1)
    fun getUser() {
        runBlockingTest {
            val user = usersRestController.getUser()
            assert(user.isEmailVerified)
            assert(user.email == "test@test.pl")
            assert(user.nick == "Test")
            assert(user.avatar == null)
        }
    }

    @Test
    @Order(2)
    fun putUser() {
        runBlockingTest {
            usersRestController.putUser(UserRequest("NewNick", null))
            val user = usersRestController.getUser()
            assert(user.isEmailVerified)
            assert(user.email == "test@test.pl")
            assert(user.nick == "NewNick")
            assert(user.avatar == null)
        }
    }
}
