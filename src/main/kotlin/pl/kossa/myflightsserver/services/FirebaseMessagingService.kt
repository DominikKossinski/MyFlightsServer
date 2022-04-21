package pl.kossa.myflightsserver.services

import com.google.firebase.messaging.BatchResponse
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.messagning.NotificationType
import pl.kossa.myflightsserver.data.models.Language
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.data.models.User
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.localization.NotificationsMessageSource

@Service("FirebaseMessagingService")
class FirebaseMessagingService {

    @Autowired
    lateinit var usersService: UsersService

    @Autowired
    lateinit var notificationsMessageSource: NotificationsMessageSource

    //TODO pass user locale
    suspend fun sendSharedFlightConfirmationMessage(sharedUserId: String, flightId: String, language: Language) {
        val user = usersService.getUserById(sharedUserId)
            ?: throw NotFoundException("User with id '$sharedUserId' not found.")
        val title =
            notificationsMessageSource.getTitle(NotificationType.USER_ACCEPTED_JOIN_REQUEST, null, language.locale)
        val body =
            notificationsMessageSource.getBody(NotificationType.USER_ACCEPTED_JOIN_REQUEST, null, language.locale)
        val message = MulticastMessage.builder()
            .addAllTokens(user.fcmTokens)
            .putData("title", title)
            .putData("body", body)
            .putData("notificationType", NotificationType.USER_ACCEPTED_JOIN_REQUEST.name)
            .putData("flightId", flightId)
            .build()
        val response = FirebaseMessaging.getInstance().sendMulticast(message)
        handleBatchResponse(user, response)
    }

    //TODO pass user locale
    suspend fun sendUserSendJoinRequestNotification(
        sharedFlight: SharedFlight,
        userName: String,
        sharedFlightId: String,
        language: Language
    ) {
        val user = usersService.getUserById(sharedFlight.ownerId)
            ?: throw NotFoundException("User with id '${sharedFlight.ownerId}' not found")
        val title =
            notificationsMessageSource.getTitle(NotificationType.USER_SEND_JOIN_REQUEST, locale = language.locale)
        val body = notificationsMessageSource.getBody(
            NotificationType.USER_SEND_JOIN_REQUEST,
            arrayOf(userName),
            locale = language.locale
        )
        val message = MulticastMessage.builder()
            .addAllTokens(user.fcmTokens)
            .putData("title", title)
            .putData("body", body)
            .putData("notificationType", NotificationType.USER_SEND_JOIN_REQUEST.name)
            .putData("sharedFlightId", sharedFlightId)
            .build()
        val response = FirebaseMessaging.getInstance().sendMulticast(message)
        handleBatchResponse(user, response)
    }


    private suspend fun handleBatchResponse(user: User, batchResponse: BatchResponse) {
        val tokensToDelete = arrayListOf<String>()
        for (pair in batchResponse.responses.withIndex()) {
            if (!pair.value.isSuccessful) {
                tokensToDelete.add(user.fcmTokens[pair.index])
            }
        }
        user.fcmTokens.removeAll(tokensToDelete.toSet())
        usersService.saveUser(user)
    }
}