package pl.kossa.myflightsserver.data.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//@Entity
//@Table(name = "Users")
@Document
data class User(
    @Id
//    @Column(name = "UserId", columnDefinition = "varchar(200)")
    val userId: String,

//    @Column(name = "Nick", columnDefinition = "varchar(200)")
    val nick: String?,

//    @Column(name = "Email", columnDefinition = "varchar(200)")
    val email: String?,

//    @OneToOne(cascade = [CascadeType.ALL])
//    @JoinColumn(name = "ImageId", referencedColumnName = "ImageId")
    val image: Image?
)
