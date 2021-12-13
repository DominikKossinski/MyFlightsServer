package pl.kossa.myflightsserver.retrofit.models

import pl.kossa.myflightsserver.data.models.ofp.OFPAlternate

data class SimbriefAlternate(
    val airport: SimbriefAirport,
    val route: String
) {
    fun toOFPAlternate(): OFPAlternate {
        return OFPAlternate(airport.toOFPAirport(), route)
    }
}
