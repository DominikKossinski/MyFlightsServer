package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotNull

//@Entity
//@Table(name = "Images")
@Document
data class Image(
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val imageId: String,

//    @Column(name = "Url", columnDefinition = "varchar(200)")
    @NotNull
    val url: String,

//    @Column(name = "ThumbnailUrl", columnDefinition = "varchar(200)")
    @NotNull
    val thumbnailUrl: String,

    @NotNull
    val userId: String
)
