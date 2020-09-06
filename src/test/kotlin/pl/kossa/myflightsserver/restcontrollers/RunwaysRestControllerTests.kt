package pl.kossa.myflightsserver.restcontrollers

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import pl.kossa.myflightsserver.config.DataSourceTestConfig
import pl.kossa.myflightsserver.config.FirebaseTestConfig
import pl.kossa.myflightsserver.config.RestControllersTestConfig
import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.data.models.Runway
import pl.kossa.myflightsserver.data.requests.AirportRequest
import pl.kossa.myflightsserver.data.requests.RunwayRequest
import pl.kossa.myflightsserver.exceptions.NotFoundException

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RunwaysRestControllerTests {

    @Autowired
    private lateinit var runwaysRestController: RunwaysRestController

    @Autowired
    private lateinit var airportsRestController: AirportsRestController

    private val airport = Airport(1, "Okencie", "Warsaw", "EPWA", "119.50", "119.00", null, "1")
    private val runwayToPost = Runway(1, "36L", 3300, 357, null, null, airport)
    private val runwayToPut = Runway(1, "35L", 3500, 350, "119.50", "url", airport)

    @Test
    @Order(1)
    fun addAirport() {
        airportsRestController.postAirport(AirportRequest(airport.name, airport.city, airport.shortcut, airport.towerFrequency, airport.groundFrequency, airport.imageUrl))
    }

    @Test
    @Order(2)
    fun noRunwaysOnStart() {
        val response = runwaysRestController.getRunwaysByAirportId(airport.airportId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.isEmpty() ?: false)
    }

    @Test
    @Order(3)
    fun runwayNotFound() {
        assertThrows<NotFoundException> {
            runwaysRestController.getRunwayById(1)
        }
    }

    @Test
    @Order(4)
    fun postAirplane() {
        runwaysRestController.postRunway(RunwayRequest(runwayToPost.name, runwayToPost.length, runwayToPost.heading, runwayToPost.ilsFrequency, runwayToPost.imageUrl, runwayToPost.airport.airportId))
    }

    @Test
    @Order(5)
    fun getRunway() {
        val response = runwaysRestController.getRunwayById(runwayToPost.runwayId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val runway = response.body!!
        checkRunways(runway, runwayToPost)
    }

    @Test
    @Order(6)
    fun getRunwaysByAirport() {
        val response = runwaysRestController.getRunwaysByAirportId(airport.airportId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        assert(response.body!!.isNotEmpty())
        val runway = response.body!![0]
        checkRunways(runway, runwayToPost)
    }

    @Test
    @Order(7)
    fun putRunway() {
        runwaysRestController.putRunway(
                runwayToPut.runwayId,
                RunwayRequest(runwayToPut.name, runwayToPut.length, runwayToPut.heading, runwayToPut.ilsFrequency, runwayToPut.imageUrl, runwayToPut.airport.airportId)
        )
        val response = runwaysRestController.getRunwayById(runwayToPut.runwayId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val runway = response.body!!
        checkRunways(runway, runwayToPut)
    }

    @Test
    @Order(8)
    fun deleteRunway() {
        runwaysRestController.deleteRunway(runwayToPost.runwayId)
        assertThrows<NotFoundException> {
            runwaysRestController.deleteRunway(runwayToPost.runwayId)
        }
    }

    @Test
    @Order(9)
    fun deleteAirport() {
        airportsRestController.deleteAirport(airport.airportId)
    }

    private fun checkRunways(runway: Runway, checkRunway: Runway) {
        assert(runway.runwayId == checkRunway.runwayId)
        assert(runway.name == checkRunway.name)
        assert(runway.length == checkRunway.length)
        assert(runway.heading == checkRunway.heading)
        assert(runway.ilsFrequency == checkRunway.ilsFrequency)
        assert(runway.imageUrl == checkRunway.imageUrl)
        assert(runway.airport == checkRunway.airport)
    }
}

