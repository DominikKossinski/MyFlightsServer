package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.responses.StatisticsResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.extensions.getMostFrequentKey
import pl.kossa.myflightsserver.extensions.getOccurrencesCount
import pl.kossa.myflightsserver.extensions.roundTo
import pl.kossa.myflightsserver.services.AirplanesService
import pl.kossa.myflightsserver.services.AirportsService
import pl.kossa.myflightsserver.services.FlightsService

@RestController
@RequestMapping("/api/statistics")
class StatisticsRestController : BaseRestController() {

    @Autowired
    private lateinit var flightsService: FlightsService

    @Autowired
    private lateinit var airportsService: AirportsService

    @Autowired
    private lateinit var airplanesService: AirplanesService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = NotFoundError::class))]
            )
        ]
    )
    suspend fun getUserStats(): StatisticsResponse {
        val user = getUserDetails()
        val flights = flightsService.getFlightsByUserId(user.uid)
        val flightHours = flights.map {
            ((it.arrivalDate.time - it.departureDate.time).toDouble() / (60f * 60f * 1_000f).toDouble()).roundTo(2)
        }.sum()

        val favoriteAirplaneId = flights.map { it.airplane.airplaneId }.getOccurrencesCount().getMostFrequentKey()
        val favoriteAirplane =
            favoriteAirplaneId?.let { airplanesService.getAirplaneById(user.uid, favoriteAirplaneId) }

        val favoriteDepartureAirportId =
            flights.map { it.departureAirport.airportId }.getOccurrencesCount().getMostFrequentKey()
        val favoriteDepartureAirport =
            favoriteDepartureAirportId?.let { airportsService.getAirportById(user.uid, favoriteDepartureAirportId) }


        val favoriteArrivalAirportId =
            flights.map { it.arrivalAirport.airportId }.getOccurrencesCount().getMostFrequentKey()
        val favoriteArrivalAirport =
            favoriteArrivalAirportId?.let { airportsService.getAirportById(user.uid, favoriteArrivalAirportId) }
        return StatisticsResponse(flightHours, favoriteAirplane, favoriteDepartureAirport, favoriteArrivalAirport)
    }
}