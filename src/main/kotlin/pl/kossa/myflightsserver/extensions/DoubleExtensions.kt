package pl.kossa.myflightsserver.extensions

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundTo(nPlaces: Int): Double {
    val factor = 10.0.pow(nPlaces.toDouble())
    return (this * factor).roundToInt().toDouble() / factor
}