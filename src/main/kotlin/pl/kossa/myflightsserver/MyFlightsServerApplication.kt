package pl.kossa.myflightsserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MyFlightsServerApplication

fun main(args: Array<String>) {
    runApplication<MyFlightsServerApplication>(*args)
}
