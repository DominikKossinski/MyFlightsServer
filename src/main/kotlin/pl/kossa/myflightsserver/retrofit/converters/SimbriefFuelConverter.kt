package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.SimbriefFuel

class SimbriefFuelConverter : Converter<SimbriefFuel> {

    override fun read(node: InputNode?): SimbriefFuel {
        var currentChild = node?.next

        var taxiV: Int? = null
        var enrouteBurnV: Int? = null
        var contingencyV: Int? = null
        var alternateBurnV: Int? = null
        var reserveV: Int? = null
        var minTakeoffV: Int? = null
        var planRampV: Int? = null
        var planLandingV: Int? = null
        var avgFuelFlowV: Int? = null
        var maxTanksV: Int? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "taxi" -> taxiV = currentChild.value?.toIntOrNull()
                "enroute_burn" -> enrouteBurnV = currentChild.value?.toIntOrNull()
                "contingency" -> contingencyV = currentChild.value?.toIntOrNull()
                "alternate_burn" -> alternateBurnV = currentChild.value?.toIntOrNull()
                "reserve" -> reserveV = currentChild.value?.toIntOrNull()
                "min_takeoff" -> minTakeoffV = currentChild.value?.toIntOrNull()
                "plan_ramp" -> planRampV = currentChild.value?.toIntOrNull()
                "plan_landing" -> planLandingV = currentChild.value?.toIntOrNull()
                "avg_fuel_flow" -> avgFuelFlowV = currentChild.value?.toIntOrNull()
                "max_tanks" -> maxTanksV = currentChild.value?.toIntOrNull()
            }
            currentChild = node?.next
        }
        val taxi = taxiV ?: throw OFPParsingException(node?.name, "taxi")
        val enrouteBurn = enrouteBurnV ?: throw  OFPParsingException(node?.name, "enroute_burn")
        val contingency = contingencyV ?: throw OFPParsingException(node?.name, "contingency")
        val alternateBurn = alternateBurnV ?: throw OFPParsingException(node?.name, "alternate_burn")
        val reserve = reserveV ?: throw  OFPParsingException(node?.name, "reserve")
        val minTakeoff = minTakeoffV ?: throw  OFPParsingException(node?.name, "min_takeoff")
        val planRamp = planRampV ?: throw OFPParsingException(node?.name, "plan_ramp")
        val planLanding = planLandingV ?: throw OFPParsingException(node?.name, "plan_landing")
        val avgFuelFlow = avgFuelFlowV ?: throw  OFPParsingException(node?.name, "avg_fuel_flow")
        val maxTanks = maxTanksV ?: throw  OFPParsingException(node?.name, "max_tanks")
        return SimbriefFuel(
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

    override fun write(node: OutputNode?, value: SimbriefFuel?) {
        TODO("Not yet implemented")
    }
}