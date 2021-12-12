package pl.kossa.myflightsserver.data.models.ofp

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class OFPFile(
    @Id
    val fileId: String,
    val fileName: String,
    val fileUrl: String
)