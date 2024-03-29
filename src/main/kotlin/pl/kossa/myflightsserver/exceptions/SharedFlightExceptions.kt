package pl.kossa.myflightsserver.exceptions

class AlreadyConfirmedException(sharedFlightId: String) : ApiException(
    "Shared flight with id '$sharedFlightId' is already confirmed"
)

class AlreadyJoinedException(sharedFlightId: String) : ApiException(
    "Shared flight with id '$sharedFlightId' has been already joined"
)

class UserNotJoinedException(sharedFlightId: String) : ApiException(
    "No user has joined shared flight with id '$sharedFlightId'"
)

class FlightAlreadySharedException(flightId: String) : ApiException(
    "Flight with id '$flightId' has been already shared"
)

class JoiningOwnFlightException : ApiException(
    "It is not possible to join own flight"
)