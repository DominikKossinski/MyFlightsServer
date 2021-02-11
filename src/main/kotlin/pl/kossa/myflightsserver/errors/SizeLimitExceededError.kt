package pl.kossa.myflightsserver.errors

class SizeLimitExceededError(private val size: String) : ApiError() {

    override val message: String
        get() = "Extended size limit"

    override val description: String
        get() = "Maximum file size $size"
}