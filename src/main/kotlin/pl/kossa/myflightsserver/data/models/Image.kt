package pl.kossa.myflightsserver.data.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Images")
data class Image(
    @Id
    @Column(name = "ImageId", columnDefinition = "varchar(200)")
    val imageId: String,

    @Column(name = "Url", columnDefinition = "varchar(200)")
    val url: String,

    @Column(name = "ThumbnailUrl", columnDefinition = "varchar(200)")
    val thumbnailUrl: String
)