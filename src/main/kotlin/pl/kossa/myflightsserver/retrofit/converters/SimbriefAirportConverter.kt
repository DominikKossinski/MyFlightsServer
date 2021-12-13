package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefAirport

class SimbriefAirportConverter : Converter<SimbriefAirport> {

    override fun read(node: InputNode?): SimbriefAirport {
        var currentChild = node?.next

        var icaoCodeV: String? = null
        var posLatV: Float? = null
        var posLongV: Float? = null
        var elevationV: Float? = null
        var nameV: String? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "icao_code" -> icaoCodeV = currentChild.value
                "pos_lat" -> posLatV = currentChild.value?.toFloatOrNull()
                "pos_long" -> posLongV = currentChild.value?.toFloatOrNull()
                "elevation" -> elevationV = currentChild.value?.toFloatOrNull()
                "name" -> nameV = currentChild.value
            }
            currentChild = node?.next
        }
        val icaoCode = icaoCodeV ?: throw OFPParsingException(node?.name, "icao_code")
        val posLat = posLatV ?: throw OFPParsingException(node?.name, "pos_lat")
        val posLong = posLongV ?: throw OFPParsingException(node?.name, "pos_long")
        val elevation = elevationV ?: throw OFPParsingException(node?.name, "elevation")
        val name = nameV ?: throw OFPParsingException(node?.name, "name")

        return SimbriefAirport(icaoCode, posLat, posLong, elevation, name)
    }

    override fun write(node: OutputNode?, value: SimbriefAirport?) {
        TODO("Not yet implemented")
    }
}