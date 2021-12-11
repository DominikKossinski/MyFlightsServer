package pl.kossa.myflightsserver.data.models.ofp

data class Airport(
    val icaoCode: String,
    val posLat: Float,
    val posLong: Float,
    val elevation: Float,
    val name: String,
)
