package pl.kossa.myflightsserver.data.models.ofp

data class Fuel(
    val taxi: Int,
    val enrouteBurn: Int,
    val contingency: Int,
    val alternateBurn: Int,
    val reserve: String,
    val minTakeoff: Int,
    val planRamp: Int,
    val planLanding: Int,
    val avgFuelFlow: Int,
    val maxTanks: Int
)
