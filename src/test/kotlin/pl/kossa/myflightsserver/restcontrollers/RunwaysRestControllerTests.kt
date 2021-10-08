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
import pl.kossa.myflightsserver.data.models.Runway
import pl.kossa.myflightsserver.data.requests.AirportRequest
import pl.kossa.myflightsserver.data.requests.RunwayRequest
import pl.kossa.myflightsserver.exceptions.NotFoundException

@ExperimentalCoroutinesApi
@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RunwaysRestControllerTests {

    @Autowired
    private lateinit var runwaysRestController: RunwaysRestController

    @Autowired
    private lateinit var airportsRestController: AirportsRestController

    private val runwayToPost = Runway("1", "36L", 3300, 357, null, HashSet(), null, "1")
    private val runwayToPut = Runway("1", "35L", 3500, 350, "119.50", HashSet(), null, "1")
    private val airport = Airport("1", "Okęcie", "Warsaw", "EPWA", "119.50", "119.00", null, HashSet(), "1")
    private var airportId = ""
    private var runwayId = ""
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
    fun addAirport() {
        runBlockingTest {
            val response = airportsRestController.postAirport(
                AirportRequest(
                    airport.name,
                    airport.city,
                    airport.icaoCode,
                    airport.towerFrequency,
                    airport.groundFrequency,
                    airport.image
                )
            )
            airportId = response.entityId
        }
    }


    @Test
    @Order(2)
    fun postRunway() {
        runBlockingTest {
            val response = runwaysRestController.postRunway(
                airportId,
                RunwayRequest(
                    runwayToPost.name,
                    runwayToPost.length,
                    runwayToPost.heading,
                    runwayToPost.ilsFrequency,
                    runwayToPost.image
                )
            )
            runwayId = response.entityId
        }
    }

    @Test
    @Order(3)
    fun getRunway() {
        runBlockingTest {
            val response = runwaysRestController.getRunwayById(runwayId)
            checkRunways(response, runwayToPost)
        }
    }

    @Test
    @Order(4)
    fun putRunway() {
        runBlockingTest {
            runwaysRestController.putRunway(
                airportId,
                runwayId,
                RunwayRequest(
                    runwayToPut.name,
                    runwayToPut.length,
                    runwayToPut.heading,
                    runwayToPut.ilsFrequency,
                    runwayToPut.image
                )
            )
        }
    }

    @Test
    @Order(5)
    fun deleteRunway() {
        runBlockingTest {
            runwaysRestController.deleteRunway(airportId, runwayId)
            assertThrows<NotFoundException> {
                runwaysRestController.deleteRunway(airportId, runwayId)
            }
        }
    }

    @Test
    @Order(6)
    fun deleteAirport() {
        runBlockingTest {
            airportsRestController.deleteAirport(airportId)
        }
    }

    private fun checkRunways(runway: Runway, checkRunway: Runway) {
        assert(runway.runwayId == runwayId)
        assert(runway.name == checkRunway.name)
        assert(runway.length == checkRunway.length)
        assert(runway.heading == checkRunway.heading)
        assert(runway.ilsFrequency == checkRunway.ilsFrequency)
        assert(runway.image == checkRunway.image)
    }
}

