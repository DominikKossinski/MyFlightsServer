package pl.kossa.myflightsserver.restcontrollers

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

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AirportsRestControllerTests {

    @Autowired
    private lateinit var airportRestController: AirportsRestController

    private val airportToPost = Airport("1", "Okencie", "Warsaw", "EPWA", "119.50", "119.00", null, HashSet(), "1")
    private val airportToPut = Airport("1", "Katowice", "Ktowice", "EPKT", "118.50", "121.00", null, HashSet(), "1")

    @Test
    @Order(1)
    suspend fun noAirportsOnStart() {
        val airports = airportRestController.getUserAirports()
        assert(airports.isEmpty())
    }

    @Test
    @Order(2)
    suspend fun airportNotFound() {
        assertThrows<NotFoundException> {
            airportRestController.getAirportById("1")
        }
    }

    @Test
    @Order(3)
    suspend fun postAirport() {
        airportRestController.postAirport(
            AirportRequest(
                airportToPost.name,
                airportToPost.city,
                airportToPost.icaoCode,
                airportToPost.towerFrequency,
                airportToPost.groundFrequency,
                airportToPost.image
            )
        )
    }

    @Test
    @Order(4)
    suspend fun getAirport() {
        val airport = airportRestController.getAirportById(airportToPost.airportId)
        checkAirports(airport, airportToPost)
    }

    @Test
    @Order(5)
    suspend fun getUserAirport() {
        val airports = airportRestController.getUserAirports()
        checkAirports(airports[0], airportToPost)
    }

    @Test
    @Order(6)
    suspend fun putAirport() {
        airportRestController.putAirport(
            airportToPut.airportId,
            AirportRequest(
                airportToPut.name,
                airportToPut.city,
                airportToPut.icaoCode,
                airportToPut.towerFrequency,
                airportToPut.groundFrequency,
                airportToPut.image
            )
        )
        val airport = airportRestController.getAirportById(airportToPut.airportId)
        checkAirports(airport, airportToPut)
    }

    @Test
    @Order(7)
    suspend fun deleteAirport() {
        airportRestController.deleteAirport(airportToPost.airportId)
        assertThrows<NotFoundException> {
            airportRestController.deleteAirport(airportToPost.airportId)
        }
    }

    private fun checkAirports(airport: Airport, checkAirport: Airport) {
        assert(airport.airportId == checkAirport.airportId)
        assert(airport.name == checkAirport.name)
        assert(airport.userId == checkAirport.userId)
        assert(airport.city == checkAirport.city)
        assert(airport.icaoCode == checkAirport.icaoCode)
        assert(airport.towerFrequency == checkAirport.towerFrequency)
        assert(airport.groundFrequency == checkAirport.groundFrequency)
        assert(airport.image == checkAirport.image)
    }
}
