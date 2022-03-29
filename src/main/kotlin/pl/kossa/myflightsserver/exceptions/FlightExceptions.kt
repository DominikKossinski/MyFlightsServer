package pl.kossa.myflightsserver.exceptions

class FlightTimeException : ApiException("Flight arrival and departure have to be in past")

class PlannedFlightTimeException : ApiException("Planed flight arrival and departure date have to be in future")

class ArrivalTimeException : ApiException("Arrival time has to be after departure time")