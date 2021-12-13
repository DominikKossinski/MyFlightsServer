package pl.kossa.myflightsserver.retrofit.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import pl.kossa.myflightsserver.data.models.ofp.OFPAirport

@Root(strict = false)
data class SimbriefAirport(
    @field:Element(name = "icao_code")
    val icaoCode: String,

    @field:Element(name = "pos_lat")
    val posLat: Float,

    @field:Element(name = "pos_long")
    val posLong: Float,

    @field:Element(name = "elevation")
    val elevation: Float,

    @field:Element(name = "name")
    val name: String,
) {

    fun toOFPAirport(): OFPAirport {
        return OFPAirport(
            icaoCode,
            posLat,
            posLong,
            elevation,
            name
        )
    }
}
