package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//@Entity
//@Table(name = "Airports")
@Document
data class Airport(
    @Id
//    @Column(name = "AirportId", columnDefinition = "int")
    val airportId: String,

//    @Column(name = "AirportName", columnDefinition = "varchar(200)")
    val name: String,

//    @Column(name = "City", columnDefinition = "varchar(200)")
    val city: String,

//    @Column(name = "IcaoCode", columnDefinition = "varchar(200)")
    val icaoCode: String,

//    @Column(name = "TowerFrequency", columnDefinition = "varchar(200)")
    val towerFrequency: String?,

//    @Column(name = "GroundFrequency", columnDefinition = "varchar(200)")
    val groundFrequency: String?,

//    @OneToOne(cascade = [CascadeType.ALL])
//    @JoinColumn(name = "ImageId", referencedColumnName = "ImageId")
    val image: Image?,

    val runways: HashSet<Runway>,

//    @Column(name = "UserId", columnDefinition = "varchar(200)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val userId: String
)
