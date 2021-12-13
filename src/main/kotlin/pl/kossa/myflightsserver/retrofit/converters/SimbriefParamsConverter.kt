package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefParams

class SimbriefParamsConverter : Converter<SimbriefParams> {

    override fun read(node: InputNode?): SimbriefParams {
        var currentChild = node?.next

        var requestIdV: Long? = null
        var userIdV: Long? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "request_id" -> requestIdV = currentChild.value?.toLongOrNull()
                "user_id" -> userIdV = currentChild.value?.toLongOrNull()
            }
            currentChild = node?.next
        }
        val requestId = requestIdV ?: throw OFPParsingException(node?.name, "request_id")
        val userId = userIdV ?: throw  OFPParsingException(node?.name, "user_id")
        return SimbriefParams(requestId, userId)
    }


    override fun write(node: OutputNode?, value: SimbriefParams?) {
        TODO("Not yet implemented")
    }
}