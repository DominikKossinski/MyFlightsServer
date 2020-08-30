package pl.kossa.myflightsserver.data.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Users")
data class User(
        @Id
        @Column(name = "UserId", columnDefinition = "varchar")
        val userId: String,

        @Column(name = "Nick", columnDefinition = "varchar")
        val nick: String?,

        @Column(name = "Email", columnDefinition = "varchar")
        val email: String?,

        @Column(name = "ImageUrl", columnDefinition = "varchar")
        val imageUrl: String?
)