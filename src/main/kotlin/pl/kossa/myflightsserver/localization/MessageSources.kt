package pl.kossa.myflightsserver.localization

import org.springframework.context.support.ReloadableResourceBundleMessageSource
import pl.kossa.myflightsserver.data.messagning.NotificationType
import java.util.*

abstract class BaseMessageSource(private val baseName: String) {

    protected val messageSource = ReloadableResourceBundleMessageSource().apply {
        setBasename(baseName)
        setDefaultEncoding("UTF-8")
    }
}

class NotificationsMessageSource : BaseMessageSource("lang/notifications/notifications") {

    fun getTitle(
        notificationType: NotificationType,
        args: Array<out Any?>? = null,
        locale: Locale = Locale.ENGLISH
    ): String {

        val titleCode = when (notificationType) {
            NotificationType.USER_SEND_JOIN_REQUEST -> USER_SEND_JOIN_REQUEST_TITLE
            NotificationType.USER_ACCEPTED_JOIN_REQUEST -> USER_ACCEPTED_REQUEST_TITLE
        }
        return messageSource.getMessage(
            titleCode, args, locale
        )
    }

    fun getBody(
        notificationType: NotificationType,
        args: Array<out Any>? = null,
        locale: Locale = Locale.ENGLISH
    ): String {
        val bodyCode = when (notificationType) {
            NotificationType.USER_SEND_JOIN_REQUEST -> USER_SEND_JOIN_REQUEST_BODY
            NotificationType.USER_ACCEPTED_JOIN_REQUEST -> USER_ACCEPTED_REQUEST_BODY
        }
        return messageSource.getMessage(
            bodyCode, args, locale
        )
    }

    companion object {
        private const val USER_SEND_JOIN_REQUEST_TITLE = "join_request.title"
        private const val USER_SEND_JOIN_REQUEST_BODY = "join_request.body"

        private const val USER_ACCEPTED_REQUEST_TITLE = "accepted_request.title"
        private const val USER_ACCEPTED_REQUEST_BODY = "accepted_request.body"
    }
}