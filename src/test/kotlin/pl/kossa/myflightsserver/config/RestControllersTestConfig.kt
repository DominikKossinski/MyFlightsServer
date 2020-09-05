package pl.kossa.myflightsserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import pl.kossa.myflightsserver.restcontrollers.AirplanesRestController


@Profile("test")
@Configuration
class RestControllersTestConfig {

    @Bean
    fun airplanesRestController(): AirplanesRestController {
        return AirplanesRestController()
    }
}