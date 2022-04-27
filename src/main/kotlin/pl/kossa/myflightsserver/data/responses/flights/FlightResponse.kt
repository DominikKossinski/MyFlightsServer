package pl.kossa.myflightsserver.data.responses.flights

import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.data.responses.sharedflights.SharedUserData

data class FlightResponse(
    val flight: Flight,
    val ownerData: SharedUserData,
    val sharedUsers: List<ShareData>
)

data class ShareData(
    val sharedFlightId: String,
    val userData: SharedUserData?,
    val isConfirmed: Boolean
)