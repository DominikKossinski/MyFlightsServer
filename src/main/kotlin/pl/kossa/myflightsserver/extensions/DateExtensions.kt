package pl.kossa.myflightsserver.extensions

import java.util.*


fun Date.plusMinutes(minutes: Long): Date {
    return this.apply {
        time += minutes * 60 * 1_000
    }
}