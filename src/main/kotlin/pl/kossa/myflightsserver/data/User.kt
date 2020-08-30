package pl.kossa.myflightsserver.data

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class User(
        val uid: String,
        @NotBlank
        @Size(min = 0, max = 5)
        val email: String,
        val isEmailVerified: Boolean
)