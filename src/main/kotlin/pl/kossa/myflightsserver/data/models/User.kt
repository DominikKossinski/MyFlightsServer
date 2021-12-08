package pl.kossa.myflightsserver.data.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @Id
    val userId: String,

    val nick: String,

    val email: String?,

    @DBRef
    val avatar: Image?,

    val fcmToken: String?
)
