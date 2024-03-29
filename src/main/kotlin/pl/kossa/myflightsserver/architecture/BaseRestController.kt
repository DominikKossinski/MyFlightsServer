package pl.kossa.myflightsserver.architecture

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.StorageOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import pl.kossa.myflightsserver.config.security.SecurityService
import pl.kossa.myflightsserver.data.UserDetails
import pl.kossa.myflightsserver.data.models.*
import pl.kossa.myflightsserver.exceptions.UnauthorizedException
import pl.kossa.myflightsserver.services.ImagesService
import pl.kossa.myflightsserver.services.SharingSettingsService
import pl.kossa.myflightsserver.services.UsersService
import java.util.*

abstract class BaseRestController {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var securityService: SecurityService

    @Autowired
    protected lateinit var usersService: UsersService

    @Autowired
    protected lateinit var imagesService: ImagesService

    @Autowired
    protected lateinit var storageOptions: StorageOptions

    @Autowired
    protected lateinit var sharingSettingsService: SharingSettingsService


    protected suspend fun getUserDetails(locale: Locale): UserDetails {
        val user = securityService.getUser() ?: throw UnauthorizedException()
        val dbUser = usersService.getUserByEmail(user.email)
        val language = Language.getFormLocale(locale)
        if (dbUser == null) {
            logger.info("Creating database user ${user.email}")
            val oldUser = usersService.getUserById(user.uid)
            val nick = oldUser?.nick ?: ""
            val avatar = oldUser?.avatar
            val regulationsAccepted = oldUser?.regulationsAccepted ?: false
            usersService.saveUser(
                User(
                    user.uid,
                    nick,
                    user.email,
                    avatar,
                    oldUser?.fcmTokens ?: arrayListOf(),
                    regulationsAccepted,
                    user.providerType,
                    language
                )
            )
            sharingSettingsService.save(
                SharingSettings(
                    user.uid,
                    SharingMode.PRIVATE,
                    SharingMode.PRIVATE,
                    SharingMode.PRIVATE,
                    SharingMode.PRIVATE
                )
            )
            return UserDetails(
                user.uid,
                user.email,
                user.isEmailVerified,
                null,
                null,
                false,
                user.providerType,
                language
            )
        }
        if (dbUser.language != language) {
            usersService.saveUser(dbUser.copy(language = language))
        }
        return UserDetails(
            user.uid,
            user.email,
            user.isEmailVerified,
            dbUser.nick,
            dbUser.avatar,
            dbUser.regulationsAccepted,
            user.providerType,
            language
        )
    }


    private fun deleteFromFireStorage(fileName: String) {
        val storage = storageOptions.service
        val blobId = BlobId.of(System.getenv("FIRESTORAGE_NAME"), fileName)
        storage.delete(blobId)
    }

    protected suspend fun deleteImage(image: Image) {
        deleteFromFireStorage(image.firestoreName)
        deleteFromFireStorage(image.firestoreName.replace(".", "thumbnail."))
        imagesService.deleteByImageId(image.imageId)
    }

}
