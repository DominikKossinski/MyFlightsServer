package pl.kossa.myflightsserver.data

import pl.kossa.myflightsserver.data.models.ProviderType

data class User(
    val uid: String,
    val email: String,
    val isEmailVerified: Boolean,
    val providerType: ProviderType
)