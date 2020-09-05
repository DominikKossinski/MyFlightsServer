package pl.kossa.myflightsserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import pl.kossa.myflightsserver.restcontrollers.AirplanesRestController
import pl.kossa.myflightsserver.restcontrollers.AirportsRestController
import pl.kossa.myflightsserver.restcontrollers.UsersRestController


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
}