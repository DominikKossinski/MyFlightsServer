package pl.kossa.myflightsserver.data.requests

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserRequest(
    @field:Size(max = 45)
    @field:NotBlank
    val nick: String,

    val imageId: String?,

    val regulationsAccepted: Boolean
)
