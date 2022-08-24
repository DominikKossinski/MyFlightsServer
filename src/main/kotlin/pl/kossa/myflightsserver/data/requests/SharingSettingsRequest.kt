package pl.kossa.myflightsserver.data.requests

import pl.kossa.myflightsserver.data.models.SharingMode

data class SharingSettingsRequest(
    val emailSharingMode: SharingMode,
    val flightHoursSharingMode: SharingMode,
    val favouriteAirplaneSharingMode: SharingMode,
    val favouriteAirportsSharingMode: SharingMode
)