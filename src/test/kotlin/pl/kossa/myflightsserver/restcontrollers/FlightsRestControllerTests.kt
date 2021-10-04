package pl.kossa.myflightsserver.restcontrollers

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

    private val departureRunway = Runway("1", "36L", 3300, 357, null, null)
    private val departureAirport =
        Airport("1", "Okęcie", "Warsaw", "EPWA", "119.50", "119.00", null, HashSet(), "1")
    private val arrivalRunway = Runway("2", "35L", 3500, 350, "119.50", null)
    private val arrivalAirport =
        Airport("2", "Katowice", "Katowice", "EPKT", "118.50", "121.00", null, HashSet(), "1")
    private val airplane = Airplane("1", "Airbus A380", 300, 200, null, "1")

    private val flightToPost =
        Flight(
            "1",
            "Note",
            1000,
            null,
            Date(),
            Date(),
            "1",
            airplane,
            departureAirport,
            departureRunway,
            arrivalAirport,
            arrivalRunway
        )
    private val flightToPut =
        Flight(
            "1",
            "Note5",
            1500,
            null,
            Date(),
            Date(),
            "1",
            airplane,
            arrivalAirport,
            arrivalRunway,
            departureAirport,
            departureRunway
        )

    @Test
    @Order(1)
    suspend fun addData() {
        airplanesRestController.postAirplane(
            AirplaneRequest(
                airplane.name,
                airplane.maxSpeed,
                airplane.weight,
                airplane.image
            )
        )

        airportsRestController.postAirport(
            AirportRequest(
                departureAirport.name,
                departureAirport.city,
                departureAirport.icaoCode,
                departureAirport.towerFrequency,
                departureAirport.groundFrequency,
                departureAirport.image
            )
        )
        airportsRestController.postAirport(
            AirportRequest(
                arrivalAirport.name,
                arrivalAirport.city,
                arrivalAirport.icaoCode,
                arrivalAirport.towerFrequency,
                arrivalAirport.groundFrequency,
                arrivalAirport.image
            )
        )

        runwaysRestController.postRunway(
            departureAirport.airportId,
            RunwayRequest(
                departureRunway.name,
                departureRunway.length,
                departureRunway.heading,
                departureRunway.ilsFrequency,
                departureRunway.image
            )
        )
        runwaysRestController.postRunway(
            arrivalAirport.airportId,
            RunwayRequest(
                arrivalRunway.name,
                arrivalRunway.length,
                arrivalRunway.heading,
                arrivalRunway.ilsFrequency,
                arrivalRunway.image
            )
        )
    }

    @Test
    @Order(2)
    suspend fun noFlightsOnStart() {
        val flights = flightsRestController.getUserFlights()
        assert(flights.isEmpty() ?: false)
    }

    @Test
    @Order(3)
    suspend fun flightNotFound() {
        assertThrows<NotFoundException> {
            flightsRestController.getFlightById("1")
        }
    }

    @Test
    @Order(4)
    suspend fun postFlight() {
        flightsRestController.postFlight(
            FlightRequest(
                flightToPost.note, flightToPost.distance, flightToPost.image,
                flightToPost.startDate, flightToPost.endDate, flightToPost.airplane.airplaneId,
                departureAirport.airportId, flightToPost.departureRunway.runwayId,
                arrivalAirport.airportId, flightToPost.arrivalRunway.runwayId
            )
        )
    }

    @Test
    @Order(5)
    suspend fun getFlight() {
        val flight = flightsRestController.getFlightById(flightToPost.flightId)
        checkFlights(flight, flightToPost)
    }

    @Test
    @Order(6)
    suspend fun getFlightsByUser() {
        val flights = flightsRestController.getUserFlights()
        checkFlights(flights[0], flightToPost)
    }

    @Test
    @Order(7)
    suspend fun putFlight() {
        flightsRestController.putFLight(
            flightToPut.flightId,
            FlightRequest(
                flightToPut.note, flightToPut.distance, flightToPut.image,
                flightToPut.startDate, flightToPut.endDate, flightToPut.airplane.airplaneId,
                flightToPut.departureAirport.airportId, flightToPut.departureRunway.runwayId,
                flightToPut.arrivalAirport.airportId, flightToPut.arrivalRunway.runwayId
            )
        )
        val flight = flightsRestController.getFlightById(flightToPut.flightId)
        checkFlights(flight, flightToPut)
    }

    @Test
    @Order(8)
    suspend fun deleteFlight() {
        flightsRestController.deleteFlight(flightToPost.flightId)
        assertThrows<NotFoundException> {
            flightsRestController.deleteFlight(flightToPost.flightId)
        }
    }

    @Test
    @Order(9)
    suspend fun deleteData() {
        airportsRestController.deleteAirport(departureAirport.airportId)
        airportsRestController.deleteAirport(arrivalAirport.airportId)

        airplanesRestController.deleteAirplane(airplane.airplaneId)

        runwaysRestController.deleteRunway(departureAirport.airportId, departureRunway.runwayId)
        runwaysRestController.deleteRunway(arrivalAirport.airportId, arrivalRunway.runwayId)
    }

    private fun checkFlights(flight: Flight, checkFlight: Flight) {
        assert(flight.flightId == checkFlight.flightId)
        assert(flight.note == checkFlight.note)
        assert(flight.distance == checkFlight.distance)
        assert(flight.image == checkFlight.image)
        assert(flight.userId == checkFlight.userId)
        assert(flight.airplane == checkFlight.airplane)
        assert(flight.departureRunway == checkFlight.departureRunway)
        assert(flight.arrivalRunway == flight.arrivalRunway)
    }
}
