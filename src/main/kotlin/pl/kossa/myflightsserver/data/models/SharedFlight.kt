package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class SharedFlight(

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val sharedFlightId: String,

    val flightId: String,

    val ownerId: String,

    val sharedUserId: String?,

    val isConfirmed: Boolean

)