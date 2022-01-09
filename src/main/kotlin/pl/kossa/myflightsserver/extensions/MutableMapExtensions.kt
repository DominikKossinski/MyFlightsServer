package pl.kossa.myflightsserver.extensions

fun MutableMap<String, Int>.getMostFrequentKey(): String? {
    return this.maxByOrNull { it.value }?.key
}