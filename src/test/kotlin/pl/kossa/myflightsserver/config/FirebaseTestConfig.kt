package pl.kossa.myflightsserver.config

import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import pl.kossa.myflightsserver.config.security.SecurityService
import pl.kossa.myflightsserver.data.User

@Profile("test")
@Configuration
class FirebaseTestConfig {

    @Bean
    fun firebaseConfig(): FirebaseConfig {
        return Mockito.mock(FirebaseConfig::class.java)
    }

    @Primary
    @Bean
    fun firebaseInit() {
        println("abc")
    }

    @Bean
    fun securityService(): SecurityService {
        val mock = Mockito.mock(SecurityService::class.java)
        Mockito.`when`(mock.getUser()).thenReturn(User("1", "test@test.pl", true))
        return mock
    }
}