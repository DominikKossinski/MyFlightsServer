package pl.kossa.myflightsserver.data

import com.google.firebase.auth.FirebaseToken

data class Credentials(
        val decodedToken: FirebaseToken,
        val tokenId: String
)