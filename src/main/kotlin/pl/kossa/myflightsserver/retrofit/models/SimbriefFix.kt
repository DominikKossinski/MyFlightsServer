package pl.kossa.myflightsserver.retrofit.models

import pl.kossa.myflightsserver.data.models.ofp.OFPWayPointType

data class SimbriefFix(
    val ident: String,
    val name: String,
    val type: OFPWayPointType,
    val posLat: Float,
    val posLong: Float,
    val viaAirway: String
    //TODO fields
)
