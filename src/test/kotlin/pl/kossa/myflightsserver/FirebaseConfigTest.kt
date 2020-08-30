package pl.kossa.myflightsserver

import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import pl.kossa.myflightsserver.config.FirebaseConfig
import javax.sql.DataSource

@Profile("test")
@Configuration
class FirebaseConfigTest {

    @Bean
    fun dataSource(): DataSource {
        return Mockito.mock(DataSource::class.java)
    }

    @Bean
    fun abc(): FirebaseConfig {
        return Mockito.mock(FirebaseConfig::class.java)
    }

    @Primary
    @Bean
    fun firebaseInit() {
        println("abc")
    }
}