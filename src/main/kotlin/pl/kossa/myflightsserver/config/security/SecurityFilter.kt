package pl.kossa.myflightsserver.config.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.multipart.MaxUploadSizeExceededException
import pl.kossa.myflightsserver.config.ApplicationConfig
import pl.kossa.myflightsserver.data.Credentials
import pl.kossa.myflightsserver.data.User
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var securityService: SecurityService

    @Autowired
    lateinit var applicationConfig: ApplicationConfig

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        verifyToken(request)
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            logger.error("${applicationConfig.size} Error $e")
            logger.error("Cause ${e.cause}")
            if (e.cause is MaxUploadSizeExceededException) {
                throw e.cause ?: e
            } else {
                throw e
            }
        }
    }

    @Throws(FirebaseAuthException::class)
    private fun verifyToken(request: HttpServletRequest) {
        val token = securityService.getBearerToken(request)
        if (token != null) {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
            decodedToken?.let {
                val user = User(it.uid, it.email, it.isEmailVerified)
                logger.info("DecodedToken: $user")
                val authentication = UsernamePasswordAuthenticationToken(user, Credentials(decodedToken, token))
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
    }

}