package pl.kossa.myflightsserver.retrofit.models

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
)
