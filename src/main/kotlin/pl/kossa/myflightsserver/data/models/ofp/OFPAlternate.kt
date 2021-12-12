package pl.kossa.myflightsserver.data.models.ofp

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ofp_alternate")
data class OFPAlternate(
    @DBRef
    val airport: OFPAirport,
    @Id
    val route: String,
)
