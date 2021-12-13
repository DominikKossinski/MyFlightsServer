package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefAirport
import pl.kossa.myflightsserver.retrofit.models.SimbriefAlternate

class SimbriefAlternateConverter : Converter<SimbriefAlternate> {

    override fun read(node: InputNode?): SimbriefAlternate {
        var currentChild = node?.next

        var icaoCodeV: String? = null
        var posLatV: Float? = null
        var posLongV: Float? = null
        var elevationV: Float? = null
        var nameV: String? = null
        var routeV: String? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "icao_code" -> icaoCodeV = currentChild.value
                "pos_lat" -> posLatV = currentChild.value?.toFloatOrNull()
                "pos_long" -> posLongV = currentChild.value?.toFloatOrNull()
                "elevation" -> elevationV = currentChild.value?.toFloatOrNull()
                "name" -> nameV = currentChild.value
                "route" -> routeV = currentChild.value
            }
            currentChild = node?.next
        }

        val icaoCode = icaoCodeV ?: throw OFPParsingException(node?.name, "icao_code")
        val posLat = posLatV ?: throw OFPParsingException(node?.name, "pos_lat")
        val posLong = posLongV ?: throw OFPParsingException(node?.name, "pos_long")
        val elevation = elevationV ?: throw OFPParsingException(node?.name, "elevation")
        val name = nameV ?: throw OFPParsingException(node?.name, "name")
        val route = routeV ?: throw OFPParsingException(node?.name, "route")

        val airport = SimbriefAirport(icaoCode, posLat, posLong, elevation, name)
        return SimbriefAlternate(airport, route)
    }

    override fun write(node: OutputNode?, value: SimbriefAlternate?) {
        TODO("Not yet implemented")
    }

}
