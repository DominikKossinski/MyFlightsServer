package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefAircraft

class SimbriefAircraftConverter : Converter<SimbriefAircraft> {

    override fun read(node: InputNode?): SimbriefAircraft {
        var currentChild = node?.next

        var icaoCodeV: String? = null
        var nameV: String? = null
        var maxPassengersV: Int? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "icaocode" -> icaoCodeV = currentChild.value
                "name" -> nameV = currentChild.value
                "max_passengers" -> maxPassengersV = currentChild.value?.toIntOrNull()
            }
            currentChild = node?.next
        }

        val icaoCode = icaoCodeV ?: throw OFPParsingException(node?.name, "icaocode")
        val name = nameV ?: throw OFPParsingException(node?.name, "name")
        val maxPassengers = maxPassengersV ?: throw  OFPParsingException(node?.name, "max_passengers")
        return SimbriefAircraft(icaoCode, name, maxPassengers)

    }

    override fun write(node: OutputNode?, value: SimbriefAircraft?) {
        TODO("Not yet implemented")
    }
}