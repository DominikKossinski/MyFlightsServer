package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Airport(
    @Id
    val airportId: String,

    val name: String,

    val city: String,

    val icaoCode: String,

    val towerFrequency: String?,

    val groundFrequency: String?,

    @DBRef
    val image: Image?,

    @DBRef
    val runways: HashSet<Runway>,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val userId: String
)
