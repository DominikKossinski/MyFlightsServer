package pl.kossa.myflightsserver.data.requests.ofp

import pl.kossa.myflightsserver.data.models.ofp.*


data class OFPRequest(
    val general: GeneralParamsRequest,
    val origin: OFPAirport,
    val destination: OFPAirport,
    val alternate: OFPAlternate,
    val navlog: List<OFPFixRequest>,
    val aircraft: OFPAircraft,
    val fuel: FuelRequest,
    val images: Images
)

data class GeneralParamsRequest(
    val icaoAirLine: String,
    val route: String
    //TODO
) {
    fun toGeneral(id: String): GeneralParams {
        return GeneralParams(id, icaoAirLine, route)
    }
}

data class FuelRequest(
    val taxi: Int,
    val enrouteBurn: Int,
    val contingency: Int,
    val alternateBurn: Int,
    val reserve: Int,
    val minTakeoff: Int,
    val planRamp: Int,
    val planLanding: Int,
    val avgFuelFlow: Int,
    val maxTanks: Int
) {
    fun toOFPFuel(id: String): OFPFuel {
        return OFPFuel(
            id,
            taxi,
            enrouteBurn,
            contingency,
            alternateBurn,
            reserve,
            minTakeoff,
            planRamp,
            planLanding,
            avgFuelFlow,
            maxTanks
        )
    }
}