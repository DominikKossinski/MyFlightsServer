package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

//
//@Entity
//@Table(name = "Airplanes")
//@EnableAutoConfiguration

@Document
data class Airplane(
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @Column(name = "AirplaneId", columnDefinition = "int")
    val airplaneId: String,

//    @Column(name = "AirplaneName", columnDefinition = "varchar(200)")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50)
    val name: String,

//    @Column(name = "MaxSpeed", columnDefinition = "int")
    @Size(min = 1, max = 500)
    val maxSpeed: Int?,

//    @Column(name = "Weight", columnDefinition = "int")
    @Size(min = 1, max = 500)
    val weight: Int?,

//    @OneToOne(cascade = [CascadeType.ALL])
//    @JoinColumn(name = "ImageId", referencedColumnName = "ImageId")
    val image: Image?,

//    @Column(name = "UserId", columnDefinition = "varchar(200)")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    val userId: String
)

