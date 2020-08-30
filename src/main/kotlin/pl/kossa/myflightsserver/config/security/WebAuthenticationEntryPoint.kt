package pl.kossa.myflightsserver.config.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import pl.kossa.myflightsserver.errors.UnauthorizedError
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class WebAuthenticationEntryPoint : AuthenticationEntryPoint {

    companion object {
        val logger = LoggerFactory.getLogger(WebAuthenticationEntryPoint::class.java)
    }

    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        logger.error("Failure $authException")
        authException?.printStackTrace()
        response?.status = HttpStatus.UNAUTHORIZED.value()
        val body = jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(UnauthorizedError())
        response?.outputStream?.println(body)
    }
}