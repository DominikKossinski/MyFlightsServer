package pl.kossa.myflightsserver.data.models.ofp

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("ofp_aircraft")
data class OFPAircraft(
    @Id
    val icaoCode: String,
    val name: String,
    val maxPassengers: Int
)
