package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.data.models.ofp.OFPWayPointType
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefFix

class SimbriefFixConverter : Converter<SimbriefFix> {

    override fun read(node: InputNode?): SimbriefFix {
        var currentChild = node?.next

        var identV: String? = null
        var nameV: String? = null
        var typeV: String? = null
        var posLatV: Float? = null
        var posLongV: Float? = null
        var viaAirwayV: String? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "ident" -> identV = currentChild.value
                "name" -> nameV = currentChild.value
                "type" -> typeV = currentChild.value
                "pos_lat" -> posLatV = currentChild.value?.toFloatOrNull()
                "pos_long" -> posLongV = currentChild.value?.toFloatOrNull()
                "via_airway" -> viaAirwayV = currentChild.value
            }
            currentChild = node?.next
        }
        val ident = identV ?: throw OFPParsingException(node?.name, "ident")
        val name = nameV ?: throw  OFPParsingException(node?.name, "name")
        val type = typeV ?: throw OFPParsingException(node?.name, "type")
        val posLat = posLatV ?: throw OFPParsingException(node?.name, "pos_lat")
        val posLong = posLongV ?: throw  OFPParsingException(node?.name, "pos_long")
        val viaAirway = viaAirwayV ?: throw OFPParsingException(node?.name, "via_airway")
        return SimbriefFix(ident, name, OFPWayPointType.valueOf(type.uppercase()), posLat, posLong, viaAirway)
    }

    override fun write(node: OutputNode?, value: SimbriefFix?) {
        TODO("Not yet implemented")
    }
}