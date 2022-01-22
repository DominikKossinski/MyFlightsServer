package pl.kossa.myflightsserver.extensions

fun MutableMap<String, Int>.getMostFrequentKey(): String? {
    return this.maxByOrNull { it.value }?.key
}

fun MutableMap<String, Int>.getTopNValues(n: Int): List<Pair<String, Int>> {
    val list = this.toList().sortedByDescending { (_, value) -> value }
    return list.filterIndexed { index, _ -> index < n }
}

