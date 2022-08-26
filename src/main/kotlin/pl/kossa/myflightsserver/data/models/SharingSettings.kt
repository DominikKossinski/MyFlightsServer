package pl.kossa.myflightsserver.data.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document

class SharingSettings(
    @Id
    val userId: String,
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