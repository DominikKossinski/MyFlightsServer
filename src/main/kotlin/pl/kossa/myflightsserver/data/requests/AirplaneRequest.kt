package pl.kossa.myflightsserver.data.requests

import javax.validation.constraints.Size

data class AirplaneRequest(

        @Size(min = 0, max = 45)
        val name: String,

        @Size(min = 0)
        val maxSpeed: Int?,

        @Size(min = 0)
        val weight: Int?,

        @Size(min = 0, max = 200)
        val imageUrl: String?
)