package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotNull

@Document
data class Image(
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val imageId: String,

    val firestoreName: String,

    @NotNull
    val url: String,

    @NotNull
    val thumbnailUrl: String,

    @NotNull
    @JsonIgnore
    val userId: String
)
