package pl.kossa.myflightsserver.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.exceptions.NotFoundException

@Service("FirebaseMessagingService")
class FirebaseMessagingService {

    @Autowired
    lateinit var usersService: UsersService

    suspend fun sendSharedFlightConfirmationMessage(sharedUserId: String) {
        val user = usersService.getUserById(sharedUserId)
            ?: throw NotFoundException("User with id '$sharedUserId' not found.")
        val message = MulticastMessage.builder()
            .addAllTokens(user.fcmTokens)
            .putData("title", "Test")
            .putData("message", "Message")
            .build()
        val response = FirebaseMessaging.getInstance().sendMulticast(message)
        val tokensToDelete = arrayListOf<String>()
        for (pair in response.responses.withIndex()) {
            if (!pair.value.isSuccessful) {
                tokensToDelete.add(user.fcmTokens[pair.index])
            }
        }
        user.fcmTokens.removeAll(tokensToDelete.toSet())
        usersService.saveUser(user)
    }
}