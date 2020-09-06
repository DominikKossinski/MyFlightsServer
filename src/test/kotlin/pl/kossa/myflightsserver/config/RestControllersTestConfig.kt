package pl.kossa.myflightsserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import pl.kossa.myflightsserver.restcontrollers.*


@Profile("test")
@Configuration
class RestControllersTestConfig {

    @Bean
    fun airplanesRestController(): AirplanesRestController {
        return AirplanesRestController()
    }

    @Bean
    fun usersRestController(): UsersRestController {
        return UsersRestController()
    }

    @Bean
    fun airportsRestController(): AirportsRestController {
        return AirportsRestController()
    }

    @Bean
    fun runwaysRestController(): RunwaysRestController {
        return RunwaysRestController()
    }

    @Bean
    fun flightsRestController(): FlightsRestController {
        return FlightsRestController()
    }
}