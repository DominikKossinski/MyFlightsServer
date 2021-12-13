package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefImages
import pl.kossa.myflightsserver.retrofit.models.SimbriefMap

class SimbriefImagesConverter : Converter<SimbriefImages> {

    private val mapConverter = SimbriefMapConverter()

    override fun read(node: InputNode?): SimbriefImages {
        var currentChild = node?.next

        var directoryV: String? = null
        val maps = arrayListOf<SimbriefMap>()
        while (currentChild != null) {
            when (currentChild.name) {
                "directory" -> directoryV = currentChild.value
                "map" -> {
                    val map = mapConverter.read(currentChild)
                    maps.add(map)
                }
            }
            currentChild = node?.next
        }
        val directory = directoryV ?: throw OFPParsingException(node?.name, "directory")
        return SimbriefImages(directory, maps)
    }

    override fun write(node: OutputNode?, value: SimbriefImages?) {
        TODO("Not yet implemented")
    }
}