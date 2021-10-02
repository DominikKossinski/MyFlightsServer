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

    @Column(name = "AirportName", columnDefinition = "varchar(200)")
    val name: String,

    @Column(name = "City", columnDefinition = "varchar(200)")
    val city: String,

    @Column(name = "IcaoCode", columnDefinition = "varchar(200)")
    val icaoCode: String,

    @Column(name = "TowerFrequency", columnDefinition = "varchar(200)")
    val towerFrequency: String?,

    @Column(name = "GroundFrequency", columnDefinition = "varchar(200)")
    val groundFrequency: String?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ImageId", referencedColumnName = "ImageId")
    val image: Image?,

    @Column(name = "UserId", columnDefinition = "varchar(200)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val userId: String
)
