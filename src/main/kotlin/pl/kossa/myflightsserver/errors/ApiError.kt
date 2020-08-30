package pl.kossa.myflightsserver.errors

abstract class ApiError {
    abstract val message: String
    abstract val description: String
}