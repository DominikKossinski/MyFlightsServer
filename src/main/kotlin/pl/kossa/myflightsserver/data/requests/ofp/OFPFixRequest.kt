package pl.kossa.myflightsserver.data.requests.ofp

import pl.kossa.myflightsserver.data.models.ofp.OFPWayPointType

data class OFPFixRequest(
    val ident: String,
    val name: String,
    val type: OFPWayPointType,
    val posLat: Float,
    val posLong: Float,
    val viaAirway: String
) {
}