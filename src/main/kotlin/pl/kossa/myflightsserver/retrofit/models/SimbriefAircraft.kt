package pl.kossa.myflightsserver.retrofit.models

import pl.kossa.myflightsserver.data.models.ofp.OFPAircraft

data class SimbriefAircraft(
    val icaoCode: String,
    val name: String,
    val maxPassengers: Int
) {
    fun toOFPAircraft(): OFPAircraft {
        return OFPAircraft(icaoCode, name, maxPassengers)
    }
}
