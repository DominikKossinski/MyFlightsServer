package pl.kossa.myflightsserver.data.requests

import pl.kossa.myflightsserver.data.models.Image
import java.util.*
import javax.validation.constraints.Past
import javax.validation.constraints.Size

data class FlightRequest(
    @Size(max = 1_000)
    val note: String?,

    @Size(min = 0)
    val distance: Int?,

    val image: Image?,

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
