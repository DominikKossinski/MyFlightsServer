package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefMap

class SimbriefMapConverter : Converter<SimbriefMap> {

    override fun read(node: InputNode?): SimbriefMap {
        var currentChild = node?.next

        var nameV: String? = null
        var linkV: String? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "name" -> nameV = currentChild.value
                "link" -> linkV = currentChild.value
            }
            currentChild = node?.next
        }
        val name = nameV ?: throw OFPParsingException(node?.name, "name")
        val link = linkV ?: throw OFPParsingException(node?.name, "link")
        return SimbriefMap(name, link)
    }

    override fun write(node: OutputNode?, value: SimbriefMap?) {
        TODO("Not yet implemented")
    }
}