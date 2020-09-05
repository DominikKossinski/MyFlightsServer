package pl.kossa.myflightsserver.data

data class User(
        val uid: String,
        val email: String,
        val isEmailVerified: Boolean
)