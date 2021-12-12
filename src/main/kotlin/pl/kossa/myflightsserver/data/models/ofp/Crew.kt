package pl.kossa.myflightsserver.data.models.ofp

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Crew(
    val pilotId: Long,
    val capitan: String,
)
