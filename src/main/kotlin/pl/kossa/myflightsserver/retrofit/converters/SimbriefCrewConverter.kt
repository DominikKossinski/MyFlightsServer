package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefCrew

class SimbriefCrewConverter : Converter<SimbriefCrew> {

    override fun read(node: InputNode?): SimbriefCrew {
        var currentChild = node?.next

        var pilotIdV: Long? = null
        var cptV: String? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "pilot_id" -> pilotIdV = currentChild.value?.toLongOrNull()
                "cpt" -> cptV = currentChild.value
            }
            currentChild = node?.next
        }
        val pilotId = pilotIdV ?: throw OFPParsingException(node?.name, "pilot_if")
        val cpt = cptV ?: throw  OFPParsingException(node?.name, "cpt")
        return SimbriefCrew(pilotId, cpt)
    }

    override fun write(node: OutputNode?, value: SimbriefCrew?) {
        TODO("Not yet implemented")
    }
}