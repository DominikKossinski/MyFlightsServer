package pl.kossa.myflightsserver.retrofit.models

data class SimbriefFix(
    val ident: String,
    val name: String,
    val type: String,
    val posLat: Float,
    val posLong: Float,
    val viaAirway: String
    //TODO fields
)
