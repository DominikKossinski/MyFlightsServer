package pl.kossa.myflightsserver.data.requests

import javax.validation.constraints.Size

data class UserRequest(
        @Size(max = 45)
        val nick: String?,

        @Size(max = 200)
        val imageUrl: String?
)