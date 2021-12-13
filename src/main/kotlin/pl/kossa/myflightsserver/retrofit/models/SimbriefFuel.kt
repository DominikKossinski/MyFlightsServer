package pl.kossa.myflightsserver.retrofit.models

import pl.kossa.myflightsserver.data.models.ofp.OFPFuel

data class SimbriefFuel(
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
    fun toOFPFuel(fuelId: String): OFPFuel {
        return OFPFuel(
            fuelId,
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
