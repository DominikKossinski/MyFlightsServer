package pl.kossa.myflightsserver.data.models.ofp

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ofp_fuel")
data class OFPFuel(
    @Id
    val fuelId: String,
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
