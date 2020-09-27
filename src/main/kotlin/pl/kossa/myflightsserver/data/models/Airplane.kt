package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "Airplanes")
data class Airplane(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
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
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        val userId: String
)