package pl.kossa.myflightsserver.config

import com.google.cloud.storage.StorageOptions
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import pl.kossa.myflightsserver.datasource.*
import pl.kossa.myflightsserver.repositories.*
import pl.kossa.myflightsserver.services.*
import javax.sql.DataSource

@Profile("test")
@Configuration
class DataSourceTestConfig {

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
    fun flightsRepository(): FlightsRepository {
        return FlightsRepositoryMock()
    }

    @Bean
    fun flightsService(): FlightsService {
        return FlightsService()
    }

    @Bean
    fun runwaysRepository(): RunwaysRepository {
        return RunwaysRepositoryMock()
    }

    @Bean
    fun runwaysService(): RunwaysService {
        return RunwaysService()
    }


    @Bean
    fun imagesRepository(): ImagesRepository {
        return ImagesRepositoryMock()
    }

    @Bean
    fun imagesService(): ImagesService {
        return ImagesService()
    }

    @Bean
    fun storageOptions(): StorageOptions {
        return mock(StorageOptions::class.java)
    }

    @Bean
    fun sharedFlightsRepository(): SharedFlightsRepository {
        return SharedFlightsRepositoryMock()
    }

    @Bean
    fun sharedFlightsService(): SharedFlightsService {
        return SharedFlightsService()
    }

    @Bean
    fun sharingSettingsRepository(): SharingSettingsRepository {
        return SharingSettingsRepositoryMock()
    }

    @Bean
    fun sharingSettingsService(): SharingSettingsService {
        return SharingSettingsService()
    }

    @Bean
    fun statisticsService(): StatisticsService {
        return StatisticsService()
    }
}
