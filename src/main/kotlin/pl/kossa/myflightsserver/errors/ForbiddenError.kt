package pl.kossa.myflightsserver.errors

class ForbiddenError : ApiError() {
    override val message: String
        get() = "Forbidden"

    override val description: String
        get() = "Forbidden"
}