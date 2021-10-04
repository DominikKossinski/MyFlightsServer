package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
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

    val image: Image?,

    @Past
    val startDate: Date,

    @Past
    val endDate: Date,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @NotNull
    val userId: String,

    val airplane: Airplane,

    val departureAirport: Airport,

    val departureRunway: Runway,


    val arrivalAirport: Airport,

    val arrivalRunway: Runway
)
