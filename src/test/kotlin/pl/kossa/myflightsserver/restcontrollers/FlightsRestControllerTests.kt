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
import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.data.models.Runway
import pl.kossa.myflightsserver.data.requests.AirplaneRequest
import pl.kossa.myflightsserver.data.requests.AirportRequest
import pl.kossa.myflightsserver.data.requests.FlightRequest
import pl.kossa.myflightsserver.data.requests.RunwayRequest
import pl.kossa.myflightsserver.exceptions.NotFoundException
import java.util.*

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class FlightsRestControllerTests {

    @Autowired
    private lateinit var flightsRestController: FlightsRestController

    @Autowired
    private lateinit var airportsRestController: AirportsRestController

    @Autowired
    private lateinit var runwaysRestController: RunwaysRestController

    @Autowired
    private lateinit var airplanesRestController: AirplanesRestController

    private val departureAirport = Airport(1, "Okencie", "Warsaw", "EPWA", "119.50", "119.00", null, "1")
    private val arrivalAirport = Airport(2, "Katowice", "Ktowice", "EPKT", "118.50", "121.00", null, "1")
    private val departureRunway = Runway(1, "36L", 3300, 357, null, null, departureAirport)
    private val arrivalRunway = Runway(2, "35L", 3500, 350, "119.50", "url", arrivalAirport)
    private val airplane = Airplane(1, "Airbus A380", 300, 200, null, "1")

    private val flightToPost = Flight(1, "Note", 1000, null, Date(), Date(), "1", airplane, departureRunway, arrivalRunway)
    private val flightToPut = Flight(1, "Note5", 1500, null, Date(), Date(), "1", airplane, arrivalRunway, departureRunway)

    @Test
    @Order(1)
    fun addData() {
        airplanesRestController.postAirplane(AirplaneRequest(airplane.name, airplane.maxSpeed, airplane.weight, airplane.imageUrl))

        airportsRestController.postAirport(AirportRequest(departureAirport.name, departureAirport.city, departureAirport.shortcut, departureAirport.towerFrequency, departureAirport.groundFrequency, departureAirport.imageUrl))
        airportsRestController.postAirport(AirportRequest(arrivalAirport.name, arrivalAirport.city, arrivalAirport.shortcut, arrivalAirport.towerFrequency, arrivalAirport.groundFrequency, arrivalAirport.imageUrl))

        runwaysRestController.postRunway(RunwayRequest(departureRunway.name, departureRunway.length, departureRunway.heading, departureRunway.ilsFrequency, departureRunway.imageUrl, departureRunway.airport.airportId))
        runwaysRestController.postRunway(RunwayRequest(arrivalRunway.name, arrivalRunway.length, arrivalRunway.heading, arrivalRunway.ilsFrequency, arrivalRunway.imageUrl, arrivalRunway.airport.airportId))
    }

    @Test
    @Order(2)
    fun noFlightsOnStart() {
        val response = flightsRestController.getUserFlights()
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.isEmpty() ?: false)
    }

    @Test
    @Order(3)
    fun flightNotFound() {
        assertThrows<NotFoundException> {
            flightsRestController.getFlightById(1)
        }
    }

    @Test
    @Order(4)
    fun postFlight() {
        flightsRestController.postFlight(
                FlightRequest(
                        flightToPost.note, flightToPost.distance, flightToPost.imageUrl,
                        flightToPost.startDate, flightToPost.endDate, flightToPost.airplane.airplaneId,
                        flightToPost.departureRunway.runwayId, flightToPost.arrivalRunway.runwayId
                )
        )
    }

    @Test
    @Order(5)
    fun getFlight() {
        val response = flightsRestController.getFlightById(flightToPost.flightId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val flight = response.body!!
        checkFlights(flight, flightToPost)
    }

    @Test
    @Order(6)
    fun getFlightsByUser() {
        val response = flightsRestController.getUserFlights()
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        assert(response.body!!.isNotEmpty())
        val flight = response.body!![0]
        checkFlights(flight, flightToPost)
    }

    @Test
    @Order(7)
    fun putFlight() {
        flightsRestController.putFLight(
                flightToPut.flightId,
                FlightRequest(
                        flightToPut.note, flightToPut.distance, flightToPut.imageUrl,
                        flightToPut.startDate, flightToPut.endDate, flightToPut.airplane.airplaneId,
                        flightToPut.departureRunway.runwayId, flightToPut.arrivalRunway.runwayId
                )
        )
        val response = flightsRestController.getFlightById(flightToPut.flightId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        val flight = response.body!!
        checkFlights(flight, flightToPut)
    }

    @Test
    @Order(8)
    fun deleteFlight() {
        flightsRestController.deleteFlight(flightToPost.flightId)
        assertThrows<NotFoundException> {
            flightsRestController.deleteFlight(flightToPost.flightId)
        }
    }

    @Test
    @Order(9)
    fun deleteData() {
        airportsRestController.deleteAirport(departureAirport.airportId)
        airportsRestController.deleteAirport(arrivalAirport.airportId)

        airplanesRestController.deleteAirplane(airplane.airplaneId)

        runwaysRestController.deleteRunway(departureRunway.runwayId)
        runwaysRestController.deleteRunway(arrivalRunway.runwayId)
    }

    private fun checkFlights(flight: Flight, checkFlight: Flight) {
        assert(flight.flightId == checkFlight.flightId)
        assert(flight.note == checkFlight.note)
        assert(flight.distance == checkFlight.distance)
        assert(flight.imageUrl == checkFlight.imageUrl)
        assert(flight.startDate == checkFlight.startDate)
        assert(flight.endDate == checkFlight.endDate)
        assert(flight.userId == checkFlight.userId)
        assert(flight.airplane == checkFlight.airplane)
        assert(flight.departureRunway == checkFlight.departureRunway)
        assert(flight.arrivalRunway == flight.arrivalRunway)
    }
}