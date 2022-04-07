package pl.kossa.myflightsserver.data.responses.sharedflights

import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.data.models.Image

data class SharedFlightResponse(
    val sharedFlightId: String,
    val flight: Flight,
    val ownerId: String,
    val sharedUserData: SharedUserData?
)

data class SharedUserData(
    val userId: String,
    val email: String,
    val nick: String,
    val avatar: Image?,
)