package pl.kossa.myflightsserver.data.requests

import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.data.models.Image
import pl.kossa.myflightsserver.data.models.Runway
import javax.validation.constraints.Size

data class RunwayRequest(

        @Size(max = 45)
        val name: String,

        @Size(max = 45)
        val length: Int,

        @Size(min = 0, max = 360)
        val heading: Int,

        @Size(max = 45)
        val ilsFrequency: String?,

        val image: Image?,

        val airportId: Int
) {
    fun toRunway(runwayId: Int, airport: Airport): Runway {
        return Runway(runwayId, name, length, heading, ilsFrequency, image, airport)
    }
}