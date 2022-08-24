package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.responses.statistics.StatisticsResponse
import pl.kossa.myflightsserver.data.responses.statistics.TopNElement
import pl.kossa.myflightsserver.extensions.getMostFrequentKey
import pl.kossa.myflightsserver.extensions.getOccurrencesCount
import pl.kossa.myflightsserver.extensions.getTopNValues
import pl.kossa.myflightsserver.extensions.roundTo

@Service("StatisticsService")
class StatisticsService {

    @Autowired
    private lateinit var flightsService: FlightsService

    @Autowired
    private lateinit var airplanesService: AirplanesService

    @Autowired
    private lateinit var airportsService: AirportsService

    suspend fun getUserStatistics(userId: String): StatisticsResponse {
        val flights = flightsService.getFlightsByUserId(userId)
        val flightHours = flights.map {
            ((it.arrivalDate.time - it.departureDate.time).toDouble() / (60f * 60f * 1_000f).toDouble()).roundTo(2)
        }.sum()

        val airplanesOccurrences = flights.map { it.airplane.airplaneId }.getOccurrencesCount()
        val favoriteAirplaneId = airplanesOccurrences.getMostFrequentKey()
        val favoriteAirplane =
            favoriteAirplaneId?.let { airplanesService.getAirplaneById(userId, favoriteAirplaneId) }
        val top5AirplanesIds = airplanesOccurrences.getTopNValues(5)
        val top5Airplanes = top5AirplanesIds.map {
            TopNElement(
                airplanesService.getAirplaneById(userId, it.first),
                it.second
            )
        }

        val departureOccurrences =
            flights.map { it.departureAirport.airportId }.getOccurrencesCount()
        val favoriteDepartureAirportId = departureOccurrences.getMostFrequentKey()
        val favoriteDepartureAirport =
            favoriteDepartureAirportId?.let { airportsService.getAirportById(userId, favoriteDepartureAirportId) }
        val top5DepartureAirportsIds = departureOccurrences.getTopNValues(5)
        val top5Departures = top5DepartureAirportsIds.map {
            TopNElement(
                airportsService.getAirportById(userId, it.first),
                it.second
            )
        }


        val arrivalOccurrences =
            flights.map { it.arrivalAirport.airportId }.getOccurrencesCount()
        val favoriteArrivalAirportId = arrivalOccurrences.getMostFrequentKey()
        val favoriteArrivalAirport =
            favoriteArrivalAirportId?.let { airportsService.getAirportById(userId, favoriteArrivalAirportId) }
        val top5ArrivalAirportsIds = arrivalOccurrences.getTopNValues(5)
        val top5Arrivals = top5ArrivalAirportsIds.map {
            TopNElement(
                airportsService.getAirportById(userId, it.first),
                it.second
            )
        }
        return StatisticsResponse(
            flightHours,
            favoriteAirplane,
            top5Airplanes,
            favoriteDepartureAirport,
            top5Departures,
            favoriteArrivalAirport,
            top5Arrivals
        )
    }
}