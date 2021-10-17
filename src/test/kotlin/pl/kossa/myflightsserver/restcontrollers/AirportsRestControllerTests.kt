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
import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.data.requests.AirportRequest
import pl.kossa.myflightsserver.exceptions.NotFoundException

@ExperimentalCoroutinesApi
@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AirportsRestControllerTests {

    @Autowired
    private lateinit var airportRestController: AirportsRestController

    private var airportId = ""
    private val airportToPost = Airport("1", "Okencie", "Warsaw", "EPWA", "119.50", "119.00", null, HashSet(), "1")
    private val airportToPut = Airport("1", "Katowice", "Ktowice", "EPKT", "118.50", "121.00", null, HashSet(), "1")

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
    fun noAirportsOnStart() {
        runBlockingTest {
            val airports = airportRestController.getUserAirports("")
            assert(airports.isEmpty())
        }
    }

    @Test
    @Order(2)
    fun airportNotFound() {
        runBlockingTest {
            assertThrows<NotFoundException> {
                airportRestController.getAirportById("1")
            }
        }
    }

    @Test
    @Order(3)
    fun postAirport() {
        runBlockingTest {
            val response = airportRestController.postAirport(
                AirportRequest(
                    airportToPost.name,
                    airportToPost.city,
                    airportToPost.icaoCode,
                    airportToPost.towerFrequency,
                    airportToPost.groundFrequency,
                    airportToPost.image?.imageId
                )
            )
            airportId = response.entityId
        }
    }

    @Test
    @Order(4)
    fun getAirport() {
        runBlockingTest {
            val airport = airportRestController.getAirportById(airportId)
            checkAirports(airport, airportToPost)
        }
    }

    @Test
    @Order(5)
    fun getUserAirport() {
        runBlockingTest {
            val airports = airportRestController.getUserAirports("")
            checkAirports(airports[0], airportToPost)
        }
    }

    @Test
    @Order(6)
    fun putAirport() {
        runBlockingTest {
            airportRestController.putAirport(
                airportId,
                AirportRequest(
                    airportToPut.name,
                    airportToPut.city,
                    airportToPut.icaoCode,
                    airportToPut.towerFrequency,
                    airportToPut.groundFrequency,
                    airportToPut.image?.imageId
                )
            )
            val airport = airportRestController.getAirportById(airportId)
            checkAirports(airport, airportToPut)
        }
    }

    @Test
    @Order(7)
    fun deleteAirport() {
        runBlockingTest {
            airportRestController.deleteAirport(airportId)
            assertThrows<NotFoundException> {
                airportRestController.deleteAirport(airportId)
            }
        }
    }

    private fun checkAirports(airport: Airport, checkAirport: Airport) {
        assert(airport.airportId == airportId)
        assert(airport.name == checkAirport.name)
        assert(airport.userId == checkAirport.userId)
        assert(airport.city == checkAirport.city)
        assert(airport.icaoCode == checkAirport.icaoCode)
        assert(airport.towerFrequency == checkAirport.towerFrequency)
        assert(airport.groundFrequency == checkAirport.groundFrequency)
        assert(airport.image == checkAirport.image)
    }
}
