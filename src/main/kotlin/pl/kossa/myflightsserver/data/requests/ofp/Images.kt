package pl.kossa.myflightsserver.data.requests.ofp

data class Images(
    val directory: String,
    val maps: List<Map>
)

data class Map(
    val name: String,
    val link: String
)
