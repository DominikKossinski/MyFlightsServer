package pl.kossa.myflightsserver.exceptions

abstract class ApiException(override val message: String?) : Exception()