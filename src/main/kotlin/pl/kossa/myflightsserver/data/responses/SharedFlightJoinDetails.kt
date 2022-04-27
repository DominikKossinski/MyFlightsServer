package pl.kossa.myflightsserver.data.responses

import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedUserData

data class SharedFlightJoinDetails(
    val flight: Flight,
    val ownerData: SharedUserData
)
