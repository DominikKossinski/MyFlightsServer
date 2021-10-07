package pl.kossa.myflightsserver.restcontrollers

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

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AirplanesRestControllerTests {

    @Autowired
    private lateinit var airplanesRestController: AirplanesRestController

    private val airplaneToPost = Airplane("1", "Airbus A380", 300, 200, null, "1")
    private val airplaneToPut = Airplane("1", "Airbus A320", 200, 300, null, "1")

    @Test
    @Order(1)
    suspend fun noAirplanesOnStart() {
        val response = airplanesRestController.getUserAirplanes("")
        assert(response.isEmpty())
    }

    @Test
    @Order(2)
    suspend fun airplaneNotFound() {
        assertThrows<NotFoundException> {
            airplanesRestController.getAirplaneById("1")
        }
    }

    @Test
    @Order(3)
    suspend fun postAirplane() {
        airplanesRestController.postAirplane(
            AirplaneRequest(
                airplaneToPost.name,
                airplaneToPost.maxSpeed,
                airplaneToPost.weight,
                airplaneToPost.image
            )
        )
    }

    @Test
    @Order(4)
    suspend fun getAirplane() {
        val airplane = airplanesRestController.getAirplaneById(airplaneToPost.airplaneId)
        checkAirplanes(airplane, airplaneToPost)
    }

    @Test
    @Order(5)
    suspend fun getUserAirplanes() {
        val airplanes = airplanesRestController.getUserAirplanes("")
        checkAirplanes(airplanes[0], airplaneToPost)
    }

    @Test
    @Order(6)
    suspend fun putAirplane() {
        airplanesRestController.putAirplane(
            airplaneToPut.airplaneId,
            AirplaneRequest(airplaneToPut.name, airplaneToPut.maxSpeed, airplaneToPut.weight, airplaneToPut.image)
        )
        val airplane = airplanesRestController.getAirplaneById(airplaneToPut.airplaneId)
        checkAirplanes(airplane, airplaneToPut)
    }

    @Test
    @Order(7)
    suspend fun deleteAirplane() {
        airplanesRestController.deleteAirplane(airplaneToPost.airplaneId)
        assertThrows<NotFoundException> {
            airplanesRestController.deleteAirplane(airplaneToPost.airplaneId)
        }
    }

    private fun checkAirplanes(airplane: Airplane, checkAirplane: Airplane) {
        assert(airplane.airplaneId == checkAirplane.airplaneId)
        assert(airplane.name == checkAirplane.name)
        assert(airplane.maxSpeed == checkAirplane.maxSpeed)
        assert(airplane.weight == checkAirplane.weight)
        assert(airplane.image == checkAirplane.image)
    }
}
