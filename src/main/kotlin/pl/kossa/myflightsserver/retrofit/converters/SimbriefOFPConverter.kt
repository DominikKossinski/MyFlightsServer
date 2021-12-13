package pl.kossa.myflightsserver.retrofit.converters

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import pl.kossa.myflightsserver.retrofit.exceptions.OFPParsingException
import pl.kossa.myflightsserver.retrofit.models.*

class SimbriefOFPConverter : Converter<SimbriefOFP> {

    private val paramsConverter = SimbriefParamsConverter()
    private val generalConverter = SimbriefGeneralConverter()
    private val airportConverter = SimbriefAirportConverter()
    private val alternateConverter = SimbriefAlternateConverter()
    private val fixConverter = SimbriefFixConverter()
    private val aircraftConverter = SimbriefAircraftConverter()
    private val fuelConverter = SimbriefFuelConverter()
    private val crewConverter = SimbriefCrewConverter()
    private val imagesConverter = SimbriefImagesConverter()

    override fun read(node: InputNode?): SimbriefOFP {
        var currentChild = node?.next

        var paramsV: SimbriefParams? = null
        var generalV: SimbriefGeneral? = null
        var originV: SimbriefAirport? = null
        var destinationV: SimbriefAirport? = null
        var alternateV: SimbriefAlternate? = null
        val navLog = arrayListOf<SimbriefFix>()
        var aircraftV: SimbriefAircraft? = null
        var fuelV: SimbriefFuel? = null
        var crewV: SimbriefCrew? = null
        var imagesV: SimbriefImages? = null
        while (currentChild != null) {
            when (currentChild.name) {
                "params" -> paramsV = paramsConverter.read(currentChild)
                "general" -> generalV = generalConverter.read(currentChild)
                "origin" -> originV = airportConverter.read(currentChild)
                "destination" -> destinationV = airportConverter.read(currentChild)
                "alternate" -> alternateV = alternateConverter.read(currentChild)
                "navlog" -> {
                    var currentFix = currentChild.next
                    while (currentFix != null) {
                        navLog.add(fixConverter.read(currentFix))
                        currentFix = currentChild.next
                    }
                }
                "aircraft" -> aircraftV = aircraftConverter.read(currentChild)
                "fuel" -> fuelV = fuelConverter.read(currentChild)
                "crew" -> crewV = crewConverter.read(currentChild)
                "images" -> imagesV = imagesConverter.read(currentChild)
            }
            currentChild = node?.next
        }
        val params = paramsV ?: throw OFPParsingException(node?.name, "params")
        val general = generalV ?: throw OFPParsingException(node?.name, "general")
        val origin = originV ?: throw  OFPParsingException(node?.name, "origin")
        val destination = destinationV ?: throw OFPParsingException(node?.name, "destination")
        val alternate = alternateV ?: throw OFPParsingException(node?.name, "alternate")
        val aircraft = aircraftV ?: throw OFPParsingException(node?.name, "aircraft")
        val fuel = fuelV ?: throw OFPParsingException(node?.name, "fuel")
        val crew = crewV ?: throw OFPParsingException(node?.name, "crew")
        val images = imagesV ?: throw OFPParsingException(node?.name, "images")
        return SimbriefOFP(params, general, origin, destination, alternate, navLog, aircraft, fuel, crew, images)
    }

    override fun write(node: OutputNode?, value: SimbriefOFP?) {
        TODO("Not yet implemented")
    }
}