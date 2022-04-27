package pl.kossa.myflightsserver.restcontrollers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

@ExperimentalCoroutinesApi
@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseTestConfig::class, DataSourceTestConfig::class, RestControllersTestConfig::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlightsRestControllerTests {

    @Autowired
    private lateinit var flightsRestController: FlightsRestController

    @Autowired
    private lateinit var airportsRestController: AirportsRestController

    @Autowired
    private lateinit var runwaysRestController: RunwaysRestController

    @Autowired
    private lateinit var airplanesRestController: AirplanesRestController

    private val departureRunway = Runway("1", "36L", 3300, 357, null, HashSet(), null, "1")
    private val departureAirport =
        Airport("1", "Okęcie", "Warsaw", "EPWA", "119.50", "119.00", null, HashSet(), "1")
    private var departureAirportId = ""
    private var departureRunwayId = ""

    private val arrivalRunway = Runway("2", "35L", 3500, 350, "119.50", HashSet(), null, "1")
    private val arrivalAirport =
        Airport("2", "Katowice", "Katowice", "EPKT", "118.50", "121.00", null, HashSet(), "1")
    private var arrivalAirportId = ""
    private var arrivalRunwayId = ""

    private val airplane = Airplane("1", "Airbus A380", 300, 200, null, "1")
    private var airplaneId = ""

    private var flightId = ""
    private val flightToPost =
        Flight(
            "1",
            "Note",
            1000,
            null,
            Date().apply { time -= 10 * 60 * 1_000 },
            Date().apply { time -= 5 * 60 * 1_000 },
            "1",
            airplane,
            departureAirport,
            departureRunway,
            arrivalAirport,
            arrivalRunway,
            false
        )
    private val flightToPut =
        Flight(
            "1",
            "Note5",
            1500,
            null,
            Date().apply { time -= 10 * 60 * 1_000 },
            Date().apply { time -= 5 * 60 * 1_000 },
            "1",
            airplane,
            arrivalAirport,
            arrivalRunway,
            departureAirport,
            departureRunway,
            false
        )

    private val testCoroutineDispatcher = StandardTestDispatcher()

    @BeforeAll
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @AfterAll
    fun clear() {
        Dispatchers.resetMain()
    }

    @Test
    @Order(1)
    fun addData() = runTest {
        airplaneId = airplanesRestController.postAirplane(
            AirplaneRequest(
                airplane.name,
                airplane.maxSpeed,
                airplane.weight,
                airplane.image?.imageId
            )
        ).entityId

        departureAirportId = airportsRestController.postAirport(
            AirportRequest(
                departureAirport.name,
                departureAirport.city,
                departureAirport.icaoCode,
                departureAirport.towerFrequency,
                departureAirport.groundFrequency,
                departureAirport.image?.imageId
            )
        ).entityId
        arrivalAirportId = airportsRestController.postAirport(
            AirportRequest(
                arrivalAirport.name,
                arrivalAirport.city,
                arrivalAirport.icaoCode,
                arrivalAirport.towerFrequency,
                arrivalAirport.groundFrequency,
                arrivalAirport.image?.imageId
            )
        ).entityId

        departureRunwayId = runwaysRestController.postRunway(
            departureAirportId,
            RunwayRequest(
                departureRunway.name,
                departureRunway.length,
                departureRunway.heading,
                departureRunway.ilsFrequency,
                departureRunway.image?.imageId
            )
        ).entityId
        arrivalRunwayId = runwaysRestController.postRunway(
            arrivalAirportId,
            RunwayRequest(
                arrivalRunway.name,
                arrivalRunway.length,
                arrivalRunway.heading,
                arrivalRunway.ilsFrequency,
                arrivalRunway.image?.imageId
            )
        ).entityId
    }

    @Test
    @Order(2)
    fun noFlightsOnStart() = runTest {
        val flights = flightsRestController.getUserFlights()
        assert(flights.isEmpty())
    }

    @Test
    @Order(3)
    fun flightNotFound() = runTest {
        assertThrows<NotFoundException> {
            flightsRestController.getFlightById("1")
        }
    }

    @Test
    @Order(4)
    fun postFlight() = runTest {

        flightId = flightsRestController.postFlight(
            FlightRequest(
                flightToPost.note, flightToPost.distance, flightToPost.image?.imageId,
                flightToPost.departureDate, flightToPost.arrivalDate, airplaneId,
                departureAirportId, departureRunwayId,
                arrivalAirportId, arrivalRunwayId, false
            )
        ).entityId
    }

    @Test
    @Order(5)
    fun getFlight() = runTest {
        val flightResponse = flightsRestController.getFlightById(flightId)
        checkFlights(flightResponse.flight, flightToPost, true)
    }

    @Test
    @Order(6)
    fun getFlightsByUser() = runTest {
        val flightResponses = flightsRestController.getUserFlights()
        checkFlights(flightResponses[0].flight, flightToPost, true)
    }

    @Test
    @Order(7)
    fun putFlight() = runTest {
        flightsRestController.putFlight(
            flightId,
            FlightRequest(
                flightToPut.note, flightToPut.distance, flightToPut.image?.imageId,
                flightToPut.departureDate, flightToPut.arrivalDate, airplaneId,
                arrivalAirportId, arrivalRunwayId,
                departureAirportId, departureRunwayId, false
            )
        )
        val flightResponse = flightsRestController.getFlightById(flightId)
        checkFlights(flightResponse.flight, flightToPut, false)
    }


    @Test
    @Order(8)
    fun deleteFlight() = runTest {
        flightsRestController.deleteFlight(flightId)
        assertThrows<NotFoundException> {
            flightsRestController.deleteFlight(flightId)
        }
    }

    @Test
    @Order(9)
    fun deleteData() = runTest {

        airplanesRestController.deleteAirplane(airplaneId)

        runwaysRestController.deleteRunway(departureAirportId, departureRunwayId)
        runwaysRestController.deleteRunway(arrivalAirportId, arrivalRunwayId)

        airportsRestController.deleteAirport(departureAirportId)
        airportsRestController.deleteAirport(arrivalAirportId)

    }

    private fun checkFlights(flight: Flight, checkFlight: Flight, isPost: Boolean) {
        assert(flight.flightId == flightId)
        assert(flight.note == checkFlight.note)
        assert(flight.distance == checkFlight.distance)
        assert(flight.image == checkFlight.image)
        assert(flight.userId == checkFlight.userId)
        assert(flight.airplane.airplaneId == airplaneId)
        if (isPost) {
            assert(flight.departureAirport.airportId == departureAirportId)
            assert(flight.departureRunway.runwayId == departureRunwayId)

            assert(flight.arrivalAirport.airportId == arrivalAirportId)
            assert(flight.arrivalRunway.runwayId == arrivalRunwayId)
        } else {
            assert(flight.departureAirport.airportId == arrivalAirportId)
            assert(flight.departureRunway.runwayId == arrivalRunwayId)

            assert(flight.arrivalAirport.airportId == departureAirportId)
            assert(flight.arrivalRunway.runwayId == departureRunwayId)
        }
    }
}
