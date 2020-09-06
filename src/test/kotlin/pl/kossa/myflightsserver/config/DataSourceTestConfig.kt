package pl.kossa.myflightsserver.config

import org.mockito.Mockito
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import pl.kossa.myflightsserver.datasource.AirplanesRepositoryMock
import pl.kossa.myflightsserver.datasource.AirportsRepositoryMock
import pl.kossa.myflightsserver.datasource.RunwaysRepositoryMock
import pl.kossa.myflightsserver.datasource.UsersRepositoryMock
import pl.kossa.myflightsserver.repositories.AirplanesRepository
import pl.kossa.myflightsserver.repositories.AirportsRepository
import pl.kossa.myflightsserver.repositories.RunwaysRepository
import pl.kossa.myflightsserver.repositories.UsersRepository
import pl.kossa.myflightsserver.services.AirplanesService
import pl.kossa.myflightsserver.services.AirportsService
import pl.kossa.myflightsserver.services.RunwaysService
import pl.kossa.myflightsserver.services.UsersService
import javax.sql.DataSource

@Profile("test")
@Configuration
class DataSourceTestConfig {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun dataSource(): DataSource {
        return Mockito.mock(DataSource::class.java)
    }

    @Bean
    fun airplanesRepository(): AirplanesRepository {
        return AirplanesRepositoryMock()
    }

    @Bean
    fun airplanesService(): AirplanesService {
        return AirplanesService()
    }

    @Bean
    fun usersRepository(): UsersRepository {
        return UsersRepositoryMock()
    }

    @Bean
    fun usersService(): UsersService {
        return UsersService()
    }

    @Bean
    fun airportsRepository(): AirportsRepository {
        return AirportsRepositoryMock()
    }

    @Bean
    fun airportsService(): AirportsService {
        return AirportsService()
    }

    @Bean
    fun runwaysRepository(): RunwaysRepository {
        return RunwaysRepositoryMock()
    }

    @Bean
    fun runwaysService(): RunwaysService {
        return RunwaysService()
    }

}