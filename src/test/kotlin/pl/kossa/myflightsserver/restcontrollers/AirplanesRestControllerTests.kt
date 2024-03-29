package pl.kossa.myflightsserver.restcontrollers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.kossa.myflightsserver.config.DataSourceTestConfig
import pl.kossa.myflightsserver.config.FirebaseTestConfig
import pl.kossa.myflightsserver.config.RestControllersTestConfig
import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.data.requests.AirplaneRequest
import pl.kossa.myflightsserver.exceptions.NotFoundException

@ExperimentalCoroutinesApi
@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AirplanesRestControllerTests {

    @Autowired
    private lateinit var airplanesRestController: AirplanesRestController
    private var airplaneId = ""
    private val airplaneToPost = Airplane("2", "Airbus A380", 300, 200, null, "1")
    private val airplaneToPut = Airplane("2", "Airbus A320", 200, 300, null, "1")

    private val testCoroutineDispatcher = StandardTestDispatcher()

    @BeforeAll
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @AfterAll
    fun clear() {
        Dispatchers.resetMain()
    }

    @Test
    @Order(1)
    fun noAirplanesOnStart() = runTest {
        val response = airplanesRestController.getUserAirplanes("")
        assert(response.isEmpty())
    }


    @Test
    @Order(2)
    fun airplaneNotFound() = runTest {
        assertThrows<NotFoundException> {
            airplanesRestController.getAirplaneById("1")
        }
    }


    @Test
    @Order(3)
    fun postAirplane() = runTest {
        val response = airplanesRestController.postAirplane(
            AirplaneRequest(
                airplaneToPost.name,
                airplaneToPost.maxSpeed,
                airplaneToPost.weight,
                airplaneToPost.image?.imageId
            )
        )
        airplaneId = response.entityId
    }

    @Test
    @Order(4)
    fun getAirplane() = runTest {
        val airplane = airplanesRestController.getAirplaneById(airplaneId)
        checkAirplanes(airplane, airplaneToPost)
    }

    @Test
    @Order(5)
    fun getUserAirplanes() = runTest {
        val airplanes = airplanesRestController.getUserAirplanes("")
        println(airplanes)
        checkAirplanes(airplanes[0], airplaneToPost)
    }


    @Test
    @Order(6)
    fun putAirplane() = runTest {
        airplanesRestController.putAirplane(
            airplaneId,
            AirplaneRequest(
                airplaneToPut.name,
                airplaneToPut.maxSpeed,
                airplaneToPut.weight,
                airplaneToPut.image?.imageId
            )
        )
        val airplane = airplanesRestController.getAirplaneById(airplaneId)
        checkAirplanes(airplane, airplaneToPut)
    }

    @Test
    @Order(7)
    fun deleteAirplane() = runTest {
        airplanesRestController.deleteAirplane(airplaneId)
        assertThrows<NotFoundException> {
            airplanesRestController.deleteAirplane(airplaneId)
        }
    }

    private fun checkAirplanes(airplane: Airplane, checkAirplane: Airplane) {
        assert(airplane.airplaneId == airplaneId)
        assert(airplane.name == checkAirplane.name)
        assert(airplane.maxSpeed == checkAirplane.maxSpeed)
        assert(airplane.weight == checkAirplane.weight)
        assert(airplane.image == checkAirplane.image)
    }
}
