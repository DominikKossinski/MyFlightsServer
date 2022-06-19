package pl.kossa.myflightsserver.data.models

class SharingSettings(
    val emailSharingMode: SharingMode,
    val flightHoursSharingMode: SharingMode,
    val favouriteAirplaneSharingMode: SharingMode,
    val favouriteAirportsSharingMode: SharingMode
)

enum class SharingMode {
    PRIVATE,
    CO_PILOTS,
    PUBLIC
}