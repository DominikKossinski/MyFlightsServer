package pl.kossa.myflightsserver.data.requests

import pl.kossa.myflightsserver.data.models.Image
import java.util.*
import javax.validation.constraints.Past
import javax.validation.constraints.Size

data class FlightRequest(
        @Size(max = 500)
        val note: String?,

        @Size(min = 0)
        val distance: Int?,

        val image: Image?,

        @Past
        val startDate: Date,

        @Past
        val endDate: Date,

        val airplaneId: Int,

        val departureRunwayId: Int,

        val arrivalRunwayId: Int
)