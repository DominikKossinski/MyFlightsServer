package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import javax.persistence.*

@Entity
@Table(name = "Airplanes")
@EnableAutoConfiguration
data class Airplane(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AirplaneId", columnDefinition = "int")
    val airplaneId: Int,

    @Column(name = "AirplaneName", columnDefinition = "varchar(200)")
    val name: String,

    @Column(name = "MaxSpeed", columnDefinition = "int")
    val maxSpeed: Int?,

    @Column(name = "Weight", columnDefinition = "int")
    val weight: Int?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ImageId", referencedColumnName = "ImageId")
    val image: Image?,

    @Column(name = "UserId", columnDefinition = "varchar(200)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val userId: String
)