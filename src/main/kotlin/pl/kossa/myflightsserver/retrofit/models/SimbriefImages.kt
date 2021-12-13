package pl.kossa.myflightsserver.retrofit.models

data class SimbriefImages(
    val directory: String,
    val maps: List<SimbriefMap>
)

data class SimbriefMap(
    val name: String,
    val link: String
)
