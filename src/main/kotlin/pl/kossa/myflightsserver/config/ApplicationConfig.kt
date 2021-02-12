package pl.kossa.myflightsserver.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:application.properties")
class ApplicationConfig {

    @Autowired
    lateinit var env: Environment

    val size: String?
        get() = env["spring.servlet.multipart.max-request-size"]
}