package pl.kossa.myflightsserver.data.models.ofp

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ofp_airport")
data class OFPAirport(
    @Id
    val icaoCode: String,
    val posLat: Float,
    val posLong: Float,
    val elevation: Float,
    val name: String,
)
