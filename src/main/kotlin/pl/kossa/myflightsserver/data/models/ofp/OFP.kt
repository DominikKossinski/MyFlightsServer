package pl.kossa.myflightsserver.data.models.ofp

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ofp")
data class OFP(
    @Id
    val ofpId: String,

    @DBRef
    val general: GeneralParams,
    @DBRef
    val origin: OFPAirport,
    @DBRef
    val destination: OFPAirport,
    @DBRef
    val alternate: OFPAlternate,
    @DBRef
    val navlog: List<OFPFix>,
    @DBRef
    val aircraft: OFPAircraft,
    @DBRef
    val fuel: OFPFuel,
    //TODO times
    //TODO weights
    //TODO impacts
//    @DBRef
//    val crew: Crew,
    //TODO notams
    //TODO weather

    //TODO files
    @DBRef
    val files: List<OFPFile>,
    //TODO api params

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val userId: String
)

@Document
data class GeneralParams(
    @Id
    val generalId: String,
    val icaoAirLine: String,
    val route: String
    //TODO
)
