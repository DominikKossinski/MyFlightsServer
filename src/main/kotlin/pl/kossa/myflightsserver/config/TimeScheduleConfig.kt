package pl.kossa.myflightsserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class TimeScheduleConfig {

    @Bean
    fun getScheduler(): ThreadPoolTaskScheduler {
        return ThreadPoolTaskScheduler()
    }
}