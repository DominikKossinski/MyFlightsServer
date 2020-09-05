package pl.kossa.myflightsserver.data.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Airports")
data class Airport(
        @Id
        @Column(name = "AirportId", columnDefinition = "int")
        val airportId: Int,

        @Column(name = "Name", columnDefinition = "varchar")
        val name: String,

        @Column(name = "City", columnDefinition = "varchar")
        val city: String,

        @Column(name = "Shortcut", columnDefinition = "varchar")
        val shortcut: String,

        @Column(name = "TowerFrequency", columnDefinition = "varchar")
        val towerFrequency: String?,

        @Column(name = "GroundFrequency", columnDefinition = "varchar")
        val groundFrequency: String?,

        @Column(name = "ImageUrl", columnDefinition = "varchar")
        val imageUrl: String?,

        @Column(name = "UserId", columnDefinition = "varchar")
        val userId: String
)