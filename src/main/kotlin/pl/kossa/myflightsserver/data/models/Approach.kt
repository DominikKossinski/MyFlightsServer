package pl.kossa.myflightsserver.data.models

import nonapi.io.github.classgraph.json.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Approach(
    @Id
    val approachId: String
)
