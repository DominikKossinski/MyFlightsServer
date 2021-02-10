package pl.kossa.myflightsserver.data.requests

import pl.kossa.myflightsserver.data.models.Image
import javax.validation.constraints.Size

data class UserRequest(
    @Size(max = 45)
    val nick: String?,

    val image: Image?
)