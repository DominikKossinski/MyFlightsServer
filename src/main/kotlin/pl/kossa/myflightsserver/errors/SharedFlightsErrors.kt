package pl.kossa.myflightsserver.errors


class AlreadyConfirmedError(errorDescription: String) : ApiError() {
    override val message: String = "AlreadyConfirmed"
    override val description: String = errorDescription
}

class AlreadyJoinedError(errorDescription: String) : ApiError() {
    override val message: String = "AlreadyJoined"
    override val description: String = errorDescription
}

class UserNotJoinedError(errorDescription: String) : ApiError() {
    override val message: String = "UserNotJoined"
    override val description: String = errorDescription
}