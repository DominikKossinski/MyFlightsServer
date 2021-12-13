package pl.kossa.myflightsserver.extensions

import java.math.BigInteger
import java.security.MessageDigest

fun md5Simbrief(origin: String, destination: String, aircraft: String): String {
    val md = MessageDigest.getInstance("md5")
    return BigInteger(1, md.digest("$origin$destination${aircraft.lowercase()}".toByteArray()))
        .toString(16)
        .padStart(32, '0').substring(0, 10)
        .uppercase()
}