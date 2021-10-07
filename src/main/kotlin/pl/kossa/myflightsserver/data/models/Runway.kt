package pl.kossa.myflightsserver.data.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Size

@Document
data class Runway(

    @Id
    val runwayId: String,

    @Size(max = 50)
    val name: String,

    @Size(max = 5_000)
    val length: Int,

    @Size(min = 0, max = 360)
    val heading: Int,

    @Size(max = 6)
    val ilsFrequency: String?,

    @DBRef
    val approaches: HashSet<Approach>,

    val image: Image?,

    val userId: String
)
