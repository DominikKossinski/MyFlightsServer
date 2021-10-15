package pl.kossa.myflightsserver.architecture

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import pl.kossa.myflightsserver.config.security.SecurityService
import pl.kossa.myflightsserver.data.UserDetails
import pl.kossa.myflightsserver.data.models.User
import pl.kossa.myflightsserver.exceptions.UnauthorizedException
import pl.kossa.myflightsserver.services.UsersService

abstract class BaseRestController {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var securityService: SecurityService

    @Autowired
    lateinit var usersService: UsersService

    protected suspend fun getUserDetails(): UserDetails {
        val user = securityService.getUser() ?: throw UnauthorizedException()
        val dbUser = usersService.getUserByEmail(user.email)
        if (dbUser == null) {
            logger.info("Creating database user ${user.email}")
            val oldUser = usersService.getUserById(user.uid)
            val nick = oldUser?.nick ?: ""
            val image = oldUser?.image
            usersService.saveUser(User(user.uid, nick, user.email, image))
            return UserDetails(user.uid, user.email, user.isEmailVerified, null, null)
        }
        return UserDetails(user.uid, user.email, user.isEmailVerified, dbUser.nick, dbUser.image)
    }
}
