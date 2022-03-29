package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past
import javax.validation.constraints.Size

@Document
data class Flight(
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val flightId: String,

    @Size(max = 1000)
    val note: String?,

    @Size(min = 1)
    val distance: Int?,

    @DBRef
    val image: Image?,

    @Past
    val departureDate: Date,

    @Past
    val arrivalDate: Date,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @NotNull
    val userId: String,

    @DBRef
    val airplane: Airplane,

    @DBRef
    val departureAirport: Airport,

    @DBRef
    val departureRunway: Runway,

    @DBRef
    val arrivalAirport: Airport,

    @DBRef
    val arrivalRunway: Runway,

    val isPlanned: Boolean
)
