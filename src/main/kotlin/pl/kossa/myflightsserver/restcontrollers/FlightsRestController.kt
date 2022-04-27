package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.UserDetails
import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.data.requests.FlightRequest
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.data.responses.flights.FlightResponse
import pl.kossa.myflightsserver.data.responses.flights.ShareData
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedUserData
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.exceptions.ArrivalTimeException
import pl.kossa.myflightsserver.exceptions.FlightTimeException
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.exceptions.PlannedFlightTimeException
import pl.kossa.myflightsserver.services.AirplanesService
import pl.kossa.myflightsserver.services.AirportsService
import pl.kossa.myflightsserver.services.FlightsService
import pl.kossa.myflightsserver.services.SharedFlightsService
import java.util.*

@RestController
@RequestMapping("/api/flights")
class FlightsRestController : BaseRestController() {

    @Autowired
    lateinit var flightsService: FlightsService

    @Autowired
    lateinit var airplanesService: AirplanesService

    @Autowired
    lateinit var airportsService: AirportsService

    @Autowired
    lateinit var sharedFlightsService: SharedFlightsService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(schema = Schema(implementation = ForbiddenError::class))]
        )]
    )
    suspend fun getUserFlights(locale: Locale = Locale.US): List<FlightResponse> {
        val user = getUserDetails(locale)
        val userFlights = flightsService.getFlightsByUserIdAndPlanned(user.uid, false)
        val sharedFlights = sharedFlightsService.getSharedFlightsBySharedUserId(user.uid).mapNotNull {
            flightsService.getFlightById(it.ownerId, it.flightId)
        }
        val allFlights = userFlights + sharedFlights
        val flightResponses = allFlights.filter { !it.isPlanned }.map { flight ->
            val sharedFlightsList =
                sharedFlightsService.getSharedFlightsByOwnerIdAndFlightId(flight.userId, flight.flightId)
            val sharedUserList = sharedFlightsList.map {
                val sharedUser = it.sharedUserId?.let { sdUId -> usersService.getUserById(sdUId) }
                val sharedUserData = sharedUser?.let { sd ->
                    SharedUserData(
                        sd.userId, sd.email, sd.nick, sd.avatar
                    )
                }
                ShareData(
                    it.sharedFlightId, sharedUserData, it.isConfirmed
                )
            }
            val ownerData = usersService.getUserById(flight.userId)?.let {
                SharedUserData(it.userId, it.email, it.nick, it.avatar)
            } ?: throw NotFoundException("Owner not found")
            FlightResponse(flight, ownerData, sharedUserList)
        }
        return flightResponses
    }

    @GetMapping("/planned", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(schema = Schema(implementation = ForbiddenError::class))]
        )]
    )
    suspend fun getUserPlannedFlights(locale: Locale = Locale.US): List<FlightResponse> {
        val user = getUserDetails(locale)
        val userFlights = flightsService.getFlightsByUserIdAndPlanned(user.uid, true)
        val sharedFlights = sharedFlightsService.getSharedFlightsBySharedUserId(user.uid).mapNotNull {
            flightsService.getFlightById(it.ownerId, it.flightId)
        }
        val allFlights = userFlights + sharedFlights
        val flightResponses = allFlights.filter { it.isPlanned }.map { flight ->
            val sharedFlightsList =
                sharedFlightsService.getSharedFlightsByOwnerIdAndFlightId(flight.userId, flight.flightId)
            val sharedUserList = sharedFlightsList.map {
                val sharedUser = it.sharedUserId?.let { sdUId -> usersService.getUserById(sdUId) }
                val sharedUserData = sharedUser?.let { sd ->
                    SharedUserData(
                        sd.userId, sd.email, sd.nick, sd.avatar
                    )
                }
                ShareData(
                    it.sharedFlightId, sharedUserData, it.isConfirmed
                )
            }
            val ownerData = usersService.getUserById(flight.userId)?.let {
                SharedUserData(it.userId, it.email, it.nick, it.avatar)
            } ?: throw NotFoundException("Owner not found")
            FlightResponse(flight, ownerData, sharedUserList)
        }
        return flightResponses
    }

    @GetMapping("/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(schema = Schema(implementation = ForbiddenError::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = [Content(schema = Schema(implementation = NotFoundError::class))]
        )]
    )
    suspend fun getFlightById(@PathVariable("flightId") flightId: String, locale: Locale = Locale.US): FlightResponse {
        val user = getUserDetails(locale)
        val flight = flightsService.getFlightById(user.uid, flightId)
            ?: sharedFlightsService.getSharedFlightBySharedUserIdAndFlightId(user.uid, flightId)?.let {
                flightsService.getFlightById(it.ownerId, flightId)
            } ?: throw NotFoundException("Flight with id '$flightId' not found.")
        val sharedFlights = sharedFlightsService.getSharedFlightsByOwnerIdAndFlightId(flight.userId, flightId)
        val sharedUserList = sharedFlights.map {
            val sharedUser = it.sharedUserId?.let { sdUId -> usersService.getUserById(sdUId) }
            val sharedUserData = sharedUser?.let { sd ->
                SharedUserData(
                    sd.userId, sd.email, sd.nick, sd.avatar
                )
            }
            ShareData(
                it.sharedFlightId, sharedUserData, it.isConfirmed
            )
        }
        val ownerData = usersService.getUserById(flight.userId)?.let {
            SharedUserData(it.userId, it.email, it.nick, it.avatar)
        } ?: throw NotFoundException("Owner not found")
        return FlightResponse(flight, ownerData, sharedUserList)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(
        value = [ApiResponse(responseCode = "201"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(schema = Schema(implementation = ForbiddenError::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = [Content(schema = Schema(implementation = NotFoundError::class))]
        )]
    )
    suspend fun postFlight(@RequestBody flightRequest: FlightRequest, locale: Locale = Locale.US): CreatedResponse {
        val user = getUserDetails(locale)
        val flight = validateFlightRequest(flightRequest, user)
        val flightAdded = flightsService.saveFlight(flight)
        return CreatedResponse(flightAdded.flightId)
    }

    @PutMapping(
        "/{flightId}", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [ApiResponse(responseCode = "204"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(schema = Schema(implementation = ForbiddenError::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = [Content(schema = Schema(implementation = NotFoundError::class))]
        )]
    )
    suspend fun putFlight(
        @PathVariable("flightId") flightId: String,
        @RequestBody flightRequest: FlightRequest,
        locale: Locale = Locale.US
    ) {
        val user = getUserDetails(locale)
        val flight = flightsService.getFlightById(user.uid, flightId)
            ?: throw NotFoundException("Flight with id '$flightId' not found.")
        if (flight.image != null && flightRequest.imageId == null) {
            deleteImage(flight.image)
        }
        val updatedFlight = validateFlightRequest(flightRequest, user, flightId)
        flightsService.saveFlight(updatedFlight)
    }

    @DeleteMapping("/{flightId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [ApiResponse(responseCode = "204"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(schema = Schema(implementation = ForbiddenError::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = [Content(schema = Schema(implementation = NotFoundError::class))]
        )]
    )
    suspend fun deleteFlight(@PathVariable("flightId") flightId: String, locale: Locale = Locale.US) {
        val user = getUserDetails(locale)
        val flight = flightsService.getFlightById(user.uid, flightId)
            ?: throw NotFoundException("Flight with id '$flightId' not found.")
        val sharedFlights = sharedFlightsService.getSharedFlightsByOwnerIdAndFlightId(user.uid, flightId)
        sharedFlights.forEach {
            sharedFlightsService.deleteSharedFlightById(it.sharedFlightId)
        }
        flight.image?.let { deleteImage(it) }
        flightsService.deleteFlightById(flightId)
    }

    private suspend fun validateFlightRequest(
        flightRequest: FlightRequest, user: UserDetails, flightId: String = UUID.randomUUID().toString()
    ): Flight {
        val image = flightRequest.imageId?.let { imagesService.getImageById(user.uid, it) }


        val airplane = airplanesService.getAirplaneById(user.uid, flightRequest.airplaneId)

        val departureAirport = airportsService.getAirportById(user.uid, flightRequest.departureAirportId)
        val departureRunway =
            departureAirport.runways.find { it.runwayId == flightRequest.departureRunwayId } ?: throw NotFoundException(
                "Runway with id '${flightRequest.departureRunwayId}' not found."
            )

        val arrivalAirport = airportsService.getAirportById(user.uid, flightRequest.arrivalAirportId)
        val arrivalRunway = arrivalAirport.runways.find { it.runwayId == flightRequest.arrivalRunwayId }
            ?: throw NotFoundException("Runway with id '${flightRequest.arrivalRunwayId}' not found.")
        if (flightRequest.arrivalDate < flightRequest.departureDate) {
            throw ArrivalTimeException()
        }
        if (flightRequest.isPlanned && (flightRequest.departureDate.before(Date()) || flightRequest.arrivalDate.before(
                Date()
            ))
        ) {
            throw PlannedFlightTimeException()
        }
        if (!flightRequest.isPlanned && (flightRequest.arrivalDate.after(Date()) || flightRequest.departureDate.after(
                Date()
            ))
        ) {
            throw FlightTimeException()
        }
        return Flight(
            flightId,
            flightRequest.note,
            flightRequest.distance,
            image,
            flightRequest.departureDate,
            flightRequest.arrivalDate,
            user.uid,
            airplane,
            departureAirport,
            departureRunway,
            arrivalAirport,
            arrivalRunway,
            flightRequest.isPlanned
        )
    }
}
