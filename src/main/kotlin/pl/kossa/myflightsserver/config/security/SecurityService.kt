package pl.kossa.myflightsserver.config.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import pl.kossa.myflightsserver.data.User
import javax.servlet.http.HttpServletRequest

@Service
class SecurityService {

    fun getBearerToken(request: HttpServletRequest): String? {
        val authorization = request.getHeader("Authorization")
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer "))
            return authorization.substring(7, authorization.length)
        return null
    }

    fun getUser(): User? {
        val principal = SecurityContextHolder.getContext().authentication.principal
        return principal as? User
    }
}