package pl.kossa.myflightsserver.data.requests

class OFPPostRequest(
    val timestamp: Long,
    val origin: String,
    val destination: String,
    val aircraft: String
)