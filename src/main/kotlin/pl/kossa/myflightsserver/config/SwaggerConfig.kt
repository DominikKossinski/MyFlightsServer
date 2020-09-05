package pl.kossa.myflightsserver.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): OpenAPI {
        return OpenAPI()
                .info(
                        Info()
                                .title("MyFlightsServer")
                                .description("Simple server to android app")
                                .version("v0.0.1")
                )

    }

}