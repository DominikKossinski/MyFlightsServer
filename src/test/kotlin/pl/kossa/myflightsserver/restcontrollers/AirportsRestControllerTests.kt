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
import pl.kossa.myflightsserver.data.requests.AirportRequest
import pl.kossa.myflightsserver.exceptions.NotFoundException

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AirportsRestControllerTests {

    @Autowired
    private lateinit var airportRestController: AirportsRestController

    private val airportToPost = Airport(1, "Okencie", "Warsaw", "EPWA", "119.50", "119.00", null, "1")
    private val airportToPut = Airport(1, "Katowice", "Ktowice", "EPKT", "118.50", "121.00", null, "1")

    @Test
    @Order(1)
    fun noAirportsOnStart() {
        val response = airportRestController.getUserAirports()
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.isEmpty() ?: false)
    }

    @Test
    @Order(2)
    fun airportNotFound() {
        assertThrows<NotFoundException> {
            airportRestController.getAirportById(1)
        }
    }

    @Test
    @Order(3)
    fun postAirport() {
        airportRestController.postAirport(AirportRequest(airportToPost.name, airportToPost.city, airportToPost.shortcut, airportToPost.towerFrequency, airportToPost.groundFrequency, airportToPost.imageUrl))
    }

    @Test
    @Order(4)
    fun getAirport() {
        val response = airportRestController.getAirportById(airportToPost.airportId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val airport = response.body!!
        checkAirports(airport, airportToPost)
    }

    @Test
    @Order(5)
    fun getUserAirport() {
        val response = airportRestController.getUserAirports()
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        assert(response.body!!.isNotEmpty())
        val airport = response.body!![0]
        checkAirports(airport, airportToPost)
    }

    @Test
    @Order(6)
    fun putAirport() {
        airportRestController.putAirport(
                airportToPut.airportId,
                AirportRequest(airportToPut.name, airportToPut.city, airportToPut.shortcut, airportToPut.towerFrequency, airportToPut.groundFrequency, airportToPut.imageUrl)
        )
        val response = airportRestController.getAirportById(airportToPut.airportId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val airport = response.body!!
        checkAirports(airport, airportToPut)
    }

    @Test
    @Order(7)
    fun deleteAirport() {
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
        assert(airport.shortcut == checkAirport.shortcut)
        assert(airport.towerFrequency == checkAirport.towerFrequency)
        assert(airport.groundFrequency == checkAirport.groundFrequency)
        assert(airport.imageUrl == checkAirport.imageUrl)
    }
}