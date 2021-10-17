package pl.kossa.myflightsserver.data.requests

import java.util.*
import javax.validation.constraints.Past
import javax.validation.constraints.Size

data class FlightRequest(
    @Size(max = 1_000)
    val note: String?,

    @Size(min = 0)
    val distance: Int?,

    val imageId: String?,

    @Past
    val departureDate: Date,

    @Past
    val arrivalDate: Date,

    val airplaneId: String,

    val departureAirportId: String,

    val departureRunwayId: String,

    val arrivalAirportId: String,

    val arrivalRunwayId: String
)
