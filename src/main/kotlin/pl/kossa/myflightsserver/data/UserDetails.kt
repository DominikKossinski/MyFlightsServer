package pl.kossa.myflightsserver.data

data class UserDetails(
        val uid: String,
        val email: String,
        val isEmailVerified: Boolean,
        val nick: String?,
        val imageUrl: String?
)