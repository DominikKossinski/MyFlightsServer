package pl.kossa.myflightsserver.restcontrollers

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import pl.kossa.myflightsserver.config.DataSourceTestConfig
import pl.kossa.myflightsserver.config.FirebaseTestConfig
import pl.kossa.myflightsserver.config.RestControllersTestConfig
import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.data.requests.AirplaneRequest
import pl.kossa.myflightsserver.exceptions.NotFoundException

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AirplanesRestControllerTests {

    @Autowired
    private lateinit var airplanesRestController: AirplanesRestController

    private val airplaneToPost = Airplane(0, "Airbus A380", 300, 200, null, "1")
    private val airplaneToPut = Airplane(0, "Airbus A320", 200, 300, "test", "1")

    @Test
    @Order(1)
    fun noAirplanesOnStart() {
        val response = airplanesRestController.getUserAirplanes()
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.isEmpty() ?: false)
    }

    @Test
    @Order(2)
    fun airplaneNotFound() {
        assertThrows<NotFoundException> {
            airplanesRestController.getAirplaneById(1)
        }
    }

    @Test
    @Order(3)
    fun postAirplane() {
        airplanesRestController.postAirplane(AirplaneRequest(airplaneToPost.name, airplaneToPost.maxSpeed, airplaneToPost.weight, airplaneToPost.imageUrl))
    }

    @Test
    @Order(4)
    fun getAirplane() {
        val response = airplanesRestController.getAirplaneById(0)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val airplane = response.body!!
        checkAirplanes(airplane, airplaneToPost)
    }

    @Test
    @Order(5)
    fun getUserAirplanes() {
        val response = airplanesRestController.getUserAirplanes()
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        assert(response.body!!.isNotEmpty())
        val airplane = response.body!![0]
        checkAirplanes(airplane, airplaneToPost)
    }

    @Test
    @Order(6)
    fun putAirplane() {
        airplanesRestController.putAirplane(
                airplaneToPut.airplaneId,
                AirplaneRequest(airplaneToPut.name, airplaneToPut.maxSpeed, airplaneToPut.weight, airplaneToPut.imageUrl)
        )
        val response = airplanesRestController.getAirplaneById(airplaneToPut.airplaneId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val airplane = response.body!!
        checkAirplanes(airplane, airplaneToPut)
    }

    private fun checkAirplanes(airplane: Airplane, checkAirplane: Airplane) {
        assert(airplane.airplaneId == checkAirplane.airplaneId)
        assert(airplane.name == checkAirplane.name)
        assert(airplane.maxSpeed == checkAirplane.maxSpeed)
        assert(airplane.weight == checkAirplane.weight)
        assert(airplane.imageUrl == checkAirplane.imageUrl)
    }
}