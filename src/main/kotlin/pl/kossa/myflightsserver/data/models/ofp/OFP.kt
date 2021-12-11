package pl.kossa.myflightsserver.data.models.ofp

data class OFP(
    val params: FlightPlanParams,
    val general: GeneralParams,
    val origin: Airport,
    val destination: Airport,
    val alternate: Alternate,
    val navlog: List<Fix>,
    val aircraft: Aircraft,
    val fuel: Fuel,
    //TODO times
    //TODO weights
    //TODO impacts
    val crew: Crew,
    //TODO notams
    //TODO weather

    //TODO files
    val images: Images
    //TODO api params
)

data class FlightPlanParams(
    val requestId: Long,
    val userId: Long,
    //TODO
)

data class GeneralParams(
    val icaoAirLine: String,
    val route: String
    //TODO
)
