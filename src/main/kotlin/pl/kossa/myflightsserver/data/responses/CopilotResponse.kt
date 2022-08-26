package pl.kossa.myflightsserver.data.responses

import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedUserData

data class CopilotResponse(
    val userData: SharedUserData?,
    val flightHours: Double?,
    val favouriteAirplane: Airplane?,
    val favouriteDepartureAirport: Airport?,
    val favouriteArrivalAirport: Airport?,
)