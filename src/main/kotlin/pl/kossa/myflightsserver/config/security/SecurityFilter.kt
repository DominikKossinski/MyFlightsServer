package pl.kossa.myflightsserver.config.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pl.kossa.myflightsserver.data.Credentials
import pl.kossa.myflightsserver.data.User
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var securityService: SecurityService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        verifyToken(request)
        filterChain.doFilter(request, response)
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