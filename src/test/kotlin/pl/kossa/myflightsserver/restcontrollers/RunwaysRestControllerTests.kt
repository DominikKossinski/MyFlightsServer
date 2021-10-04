package pl.kossa.myflightsserver.restcontrollers

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

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RunwaysRestControllerTests {

    @Autowired
    private lateinit var runwaysRestController: RunwaysRestController

    @Autowired
    private lateinit var airportsRestController: AirportsRestController

    private val runwayToPost = Runway("1", "36L", 3300, 357, null, null)
    private val runwayToPut = Runway("1", "35L", 3500, 350, "119.50", null)
    private val airport = Airport("1", "Okęcie", "Warsaw", "EPWA", "119.50", "119.00", null, HashSet(), "1")

    @Test
    @Order(1)
    suspend fun addAirport() {
        airportsRestController.postAirport(
            AirportRequest(
                airport.name,
                airport.city,
                airport.icaoCode,
                airport.towerFrequency,
                airport.groundFrequency,
                airport.image
            )
        )
    }


    @Test
    @Order(2)
    suspend fun postAirplane() {
        runwaysRestController.postRunway(
            airport.airportId,
            RunwayRequest(
                runwayToPost.name,
                runwayToPost.length,
                runwayToPost.heading,
                runwayToPost.ilsFrequency,
                runwayToPost.image
            )
        )
    }

//    @Test
//    @Order(3)
//    fun getRunway() {
//        val response = runwaysRestController.getRunwayById(runwayToPost.runwayId)
//        assert(response.statusCode == HttpStatus.OK)
//        assert(response.body != null)
//        val runway = response.body!!
//        checkRunways(runway, runwayToPost)
//    }
//
//    @Test
//    @Order(6)
//    fun getRunwaysByAirport() {
//        val response = runwaysRestController.getRunwaysByAirportId(airport.airportId)
//        assert(response.statusCode == HttpStatus.OK)
//        assert(response.body != null)
//        assert(response.body!!.isNotEmpty())
//        val runway = response.body!![0]
//        checkRunways(runway, runwayToPost)
//    }

    @Test
    @Order(3)
    suspend fun putRunway() {
        runwaysRestController.putRunway(
            airport.airportId,
            runwayToPut.runwayId,
            RunwayRequest(
                runwayToPut.name,
                runwayToPut.length,
                runwayToPut.heading,
                runwayToPut.ilsFrequency,
                runwayToPut.image
            )
        )
    }

    @Test
    @Order(4)
    suspend fun deleteRunway() {
        runwaysRestController.deleteRunway(airport.airportId, runwayToPost.runwayId)
        assertThrows<NotFoundException> {
            runwaysRestController.deleteRunway(airport.airportId, runwayToPost.runwayId)
        }
    }

    @Test
    @Order(5)
    suspend fun deleteAirport() {
        airportsRestController.deleteAirport(airport.airportId)
    }

    private fun checkRunways(runway: Runway, checkRunway: Runway) {
        assert(runway.runwayId == checkRunway.runwayId)
        assert(runway.name == checkRunway.name)
        assert(runway.length == checkRunway.length)
        assert(runway.heading == checkRunway.heading)
        assert(runway.ilsFrequency == checkRunway.ilsFrequency)
        assert(runway.image == checkRunway.image)
    }
}

