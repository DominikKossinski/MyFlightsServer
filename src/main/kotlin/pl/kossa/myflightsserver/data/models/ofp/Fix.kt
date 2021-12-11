package pl.kossa.myflightsserver.data.models.ofp

data class Fix(
    val ident: String,
    val name: String,
    val type: String,
    val posLat: Float,
    val posLong: Float,
    val viaAirway: String

    //TODO fields
)
