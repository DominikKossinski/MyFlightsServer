package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "Airports")
data class Airport(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
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
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        val userId: String
)