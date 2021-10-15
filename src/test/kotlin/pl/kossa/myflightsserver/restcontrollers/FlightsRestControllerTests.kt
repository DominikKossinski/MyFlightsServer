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
    fun addData() {
        runBlockingTest {
            airplaneId = airplanesRestController.postAirplane(
                AirplaneRequest(
                    airplane.name,
                    airplane.maxSpeed,
                    airplane.weight,
                    airplane.image
                )
            ).entityId

            departureAirportId = airportsRestController.postAirport(
                AirportRequest(
                    departureAirport.name,
                    departureAirport.city,
                    departureAirport.icaoCode,
                    departureAirport.towerFrequency,
                    departureAirport.groundFrequency,
                    departureAirport.image
                )
            ).entityId
            arrivalAirportId = airportsRestController.postAirport(
                AirportRequest(
                    arrivalAirport.name,
                    arrivalAirport.city,
                    arrivalAirport.icaoCode,
                    arrivalAirport.towerFrequency,
                    arrivalAirport.groundFrequency,
                    arrivalAirport.image
                )
            ).entityId

            departureRunwayId = runwaysRestController.postRunway(
                departureAirportId,
                RunwayRequest(
                    departureRunway.name,
                    departureRunway.length,
                    departureRunway.heading,
                    departureRunway.ilsFrequency,
                    departureRunway.image
                )
            ).entityId
            arrivalRunwayId = runwaysRestController.postRunway(
                arrivalAirportId,
                RunwayRequest(
                    arrivalRunway.name,
                    arrivalRunway.length,
                    arrivalRunway.heading,
                    arrivalRunway.ilsFrequency,
                    arrivalRunway.image
                )
            ).entityId
        }
    }

    @Test
    @Order(2)
    fun noFlightsOnStart() {
        runBlockingTest {
            val flights = flightsRestController.getUserFlights()
            assert(flights.isEmpty())
        }
    }

    @Test
    @Order(3)
    fun flightNotFound() {
        runBlockingTest {
            assertThrows<NotFoundException> {
                flightsRestController.getFlightById("1")
            }
        }
    }

    @Test
    @Order(4)
    fun postFlight() {
        runBlockingTest {
            flightId = flightsRestController.postFlight(
                FlightRequest(
                    flightToPost.note, flightToPost.distance, flightToPost.image,
                    flightToPost.departureDate, flightToPost.arrivalDate, airplaneId,
                    departureAirportId, departureRunwayId,
                    arrivalAirportId, arrivalRunwayId
                )
            ).entityId
        }
    }

    @Test
    @Order(5)
    fun getFlight() {
        runBlockingTest {
            val flight = flightsRestController.getFlightById(flightId)
            checkFlights(flight, flightToPost, true)
        }
    }

    @Test
    @Order(6)
    fun getFlightsByUser() {
        runBlockingTest {
            val flights = flightsRestController.getUserFlights()
            checkFlights(flights[0], flightToPost, true)
        }
    }

    @Test
    @Order(7)
    fun putFlight() {
        runBlockingTest {
            flightsRestController.putFLight(
                flightId,
                FlightRequest(
                    flightToPut.note, flightToPut.distance, flightToPut.image,
                    flightToPut.departureDate, flightToPut.arrivalDate, airplaneId,
                    arrivalAirportId, arrivalRunwayId,
                    departureAirportId, departureRunwayId
                )
            )
            val flight = flightsRestController.getFlightById(flightId)
            checkFlights(flight, flightToPut, false)
        }
    }

    @Test
    @Order(8)
    fun deleteFlight() {
        runBlockingTest {
            flightsRestController.deleteFlight(flightId)
            assertThrows<NotFoundException> {
                flightsRestController.deleteFlight(flightId)
            }
        }
    }

    @Test
    @Order(9)
    fun deleteData() {
        runBlockingTest {

            airplanesRestController.deleteAirplane(airplaneId)

            runwaysRestController.deleteRunway(departureAirportId, departureRunwayId)
            runwaysRestController.deleteRunway(arrivalAirportId, arrivalRunwayId)

            airportsRestController.deleteAirport(departureAirportId)
            airportsRestController.deleteAirport(arrivalAirportId)

        }
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
