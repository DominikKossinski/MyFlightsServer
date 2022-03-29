package pl.kossa.myflightsserver.errors

class ArrivalTimeError : ApiError() {
    override val message: String
        get() = "FlightTimeError"

    override val description: String
        get() = "Arrival has to be after arrival"
}

class PlannedFlightTimeError : ApiError() {
    override val message: String
        get() = "PlannedFlightError"

    override val description: String
        get() = "Planed flight arrival and departure date have to be in future"
}

class FlightTimeError : ApiError() {
    override val message: String
        get() = "FlightTimeError"

    override val description: String
        get() = "Flight arrival and departure have to be in past"
}
