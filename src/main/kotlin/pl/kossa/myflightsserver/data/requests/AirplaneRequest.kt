package pl.kossa.myflightsserver.data.requests

import pl.kossa.myflightsserver.data.models.Image
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class AirplaneRequest(

    @NotNull
    @NotBlank(message = "Name is mandatory")
    @Size(min = 0, max = 50)
    val name: String,

    @Size(min = 1, max = 500)
    val maxSpeed: Int?,

    @Size(min = 1, max = 500)
    val weight: Int?,

    val image: Image?
)
