package pl.kossa.myflightsserver.retrofit.call

data class ApiResponse<out T : Any>(val body: T?)