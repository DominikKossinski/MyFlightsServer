package pl.kossa.myflightsserver.data.models.ofp

data class Aircraft(
    val icaoCode: String,
    val name: String,
    val maxPassengers: Int
)
