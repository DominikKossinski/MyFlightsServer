package pl.kossa.myflightsserver.errors


class NotFoundError(override val description: String) : ApiError() {

    override val message: String
        get() = "Not Found"
}