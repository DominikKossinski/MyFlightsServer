package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefGeneral

class SimbriefGeneralConverter : Converter<SimbriefGeneral> {

    override fun read(node: InputNode?): SimbriefGeneral {
        var currentChild = node?.next

        var icaoAirlineV: String? = null
        var routeV: String? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "icao_airline" -> icaoAirlineV = currentChild.value
                "route" -> routeV = currentChild.value
            }
            currentChild = node?.next
        }
        val icaoAirline = icaoAirlineV ?: throw OFPParsingException(node?.name, "icao_airline")
        val route = routeV ?: throw  OFPParsingException(node?.name, "route")
        return SimbriefGeneral(icaoAirline, route)
    }

    override fun write(node: OutputNode?, value: SimbriefGeneral?) {
        TODO("Not yet implemented")
    }
}