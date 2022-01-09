package pl.kossa.myflightsserver.data.responses

import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.data.models.Airport

data class StatisticsResponse(
    val flightHours: Double,
    val favouriteAirplane: Airplane?,
    val favouriteDepartureAirport: Airport?,
    val favouriteArrivalAirport: Airport?,
)