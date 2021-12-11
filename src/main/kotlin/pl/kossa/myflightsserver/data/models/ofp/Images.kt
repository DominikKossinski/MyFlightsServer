package pl.kossa.myflightsserver.data.models.ofp

data class Images(
    val directory: String,
    val maps: List<Map>
)

data class Map(
    val name: String,
    val link: String
)
