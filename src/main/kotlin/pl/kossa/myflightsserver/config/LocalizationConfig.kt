package pl.kossa.myflightsserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.kossa.myflightsserver.localization.NotificationsMessageSource

@Configuration
class LocalizationConfig {

    @Bean
    fun notificationsMessageSource(): NotificationsMessageSource {
        return NotificationsMessageSource()
    }
}