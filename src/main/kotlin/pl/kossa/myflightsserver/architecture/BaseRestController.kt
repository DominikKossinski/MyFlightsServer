package pl.kossa.myflightsserver.architecture

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import pl.kossa.myflightsserver.config.security.SecurityService

abstract class BaseRestController {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var securityService: SecurityService

}