package pl.kossa.myflightsserver.extensions

fun List<String>.getOccurrencesCount(): MutableMap<String, Int> {
    val frequencyMap: MutableMap<String, Int> = HashMap()
    val uniqueItems = this.toTypedArray().distinct()
    for (item in uniqueItems) {
        frequencyMap[item] = this.count { it == item }
    }
    return frequencyMap
}