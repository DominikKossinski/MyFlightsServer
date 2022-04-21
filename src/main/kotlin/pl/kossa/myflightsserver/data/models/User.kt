package pl.kossa.myflightsserver.data.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class User(
    @Id
    val userId: String,

    val nick: String,

    val email: String,

    @DBRef
    val avatar: Image?,

    val fcmTokens: ArrayList<String>,

    val regulationsAccepted: Boolean,

    val providerType: ProviderType,

    val language: Language
)

enum class ProviderType {
    PASSWORD,
    GOOGLE;

    companion object {
        fun getProviderType(providerId: String): ProviderType = when (providerId) {
            "password" -> PASSWORD
            "google.com" -> GOOGLE
            else -> throw Exception("Unknown provider")
        }

    }
}

enum class Language(
    val locale: Locale
) {
    ENGLISH(Locale.US),
    GERMAN(Locale.GERMANY),
    POLISH(Locale("pl", "PL"));

    companion object {
        fun getFormLocale(locale: Locale): Language {
            return when (locale) {
                Locale.GERMANY -> GERMAN
                Locale("pl", "PL") -> POLISH
                else -> ENGLISH
            }
        }
    }
}
