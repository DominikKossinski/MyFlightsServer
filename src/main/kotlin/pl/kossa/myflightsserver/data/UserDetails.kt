package pl.kossa.myflightsserver.data

import com.fasterxml.jackson.annotation.JsonProperty
import pl.kossa.myflightsserver.data.models.Image
import pl.kossa.myflightsserver.data.models.ProviderType

data class UserDetails(
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val uid: String,
    val email: String,
    val isEmailVerified: Boolean,
    val nick: String?,
    val avatar: Image?,
    val regulationsAccepted: Boolean,
    val providerType: ProviderType
)
