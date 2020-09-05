package pl.kossa.myflightsserver.data.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Airplanes")
data class Airplane(
        @Id
        @Column(name = "AirplaneId", columnDefinition = "int")
        val airplaneId: Int,

        @Column(name = "Name", columnDefinition = "varchar")
        val name: String,

        @Column(name = "MaxSpeed", columnDefinition = "int")
        val maxSpeed: Int?,

        @Column(name = "Weight", columnDefinition = "int")
        val weight: Int?,

        @Column(name = "ImageUrl", columnDefinition = "varchar")
        val imageUrl: String?,

        @Column(name = "UserId", columnDefinition = "varchar")
        val userId: String
)