package pl.kossa.myflightsserver.errors

class UnauthorizedError : ApiError() {
    override val message: String
        get() = "Unauthorized"

    override val description: String
        get() = "Unauthorized"
}