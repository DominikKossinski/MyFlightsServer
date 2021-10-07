package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Document
data class Airplane(
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val airplaneId: String,

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50)
    val name: String,

    @Size(min = 1, max = 500)
    val maxSpeed: Int?,

    @Size(min = 1, max = 500)
    val weight: Int?,

    val image: Image?,

    @NotNull
    val userId: String
)

