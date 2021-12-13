package pl.kossa.myflightsserver.retrofit.models

import pl.kossa.myflightsserver.data.models.ofp.GeneralParams

data class SimbriefOFP(
    val params: SimbriefParams,
    val general: SimbriefGeneral,
    val origin: SimbriefAirport,
    val destination: SimbriefAirport,
    val alternate: SimbriefAlternate,
    val navlog: List<SimbriefFix>,
    val aircraft: SimbriefAircraft,
    val fuel: SimbriefFuel,
    //TODO times
    //TODO weights
    //TODO impacts
    val crew: SimbriefCrew,
    //TODO notams
    //TODO weather

    //TODO files
    val images: SimbriefImages
    //TODO api params
)

data class SimbriefParams(
    val requestId: Long,
    val userId: Long,
    //TODO
)


data class SimbriefGeneral(
    val icaoAirLine: String,
    val route: String
    //TODO
) {
    fun toOFPGeneral(generalId: String): GeneralParams {
        return GeneralParams(generalId, icaoAirLine, route)
    }
}
