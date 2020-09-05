package pl.kossa.myflightsserver.data.requests

import pl.kossa.myflightsserver.data.models.Airport
import javax.validation.constraints.Size

data class AirportRequest(
        @Size(max = 45)
        val name: String,
        @Size(max = 100)
        val city: String,
        @Size(min = 4, max = 4)
        val shortcut: String,
        @Size(max = 45)
        val towerFrequency: String?,
        @Size(max = 45)
        val groundFrequency: String?,
        @Size(max = 200)
        val imageUrl: String?
) {

    fun toAirport(airportId: Int, userId: String): Airport {
        return Airport(airportId, name, city, shortcut, towerFrequency, groundFrequency, imageUrl, userId)
    }
}