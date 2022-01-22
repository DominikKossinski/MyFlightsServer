package pl.kossa.myflightsserver.data.responses.statistics

import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.data.models.Airport

data class StatisticsResponse(
    val flightHours: Double,
    val favouriteAirplane: Airplane?,
    val top5Airplanes: List<TopNElement<Airplane>>,
    val favouriteDepartureAirport: Airport?,
    val top5DepartureAirports: List<TopNElement<Airport>>,
    val favouriteArrivalAirport: Airport?,
    val top5ArrivalAirports: List<TopNElement<Airport>>,
)