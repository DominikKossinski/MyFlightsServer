package pl.kossa.myflightsserver.data

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDetails(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        val uid: String,
        val email: String,
        val isEmailVerified: Boolean,
        val nick: String?,
        val imageUrl: String?
)