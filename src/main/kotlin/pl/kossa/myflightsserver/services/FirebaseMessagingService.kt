package pl.kossa.myflightsserver.services

import com.google.firebase.messaging.BatchResponse
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.data.models.User
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
            .putData("title", "User confirmed")
            .putData("message", "Message")
            .build()
        // TODO deeplink
        val response = FirebaseMessaging.getInstance().sendMulticast(message)
        handleBatchResponse(user, response)
    }

    suspend fun sendSharedFlightUserJoinedMessage(sharedFlight: SharedFlight) {
        val user = usersService.getUserById(sharedFlight.ownerId)
            ?: throw  NotFoundException("User with id '${sharedFlight.ownerId}' not found")
        val message = MulticastMessage.builder()
            .addAllTokens(user.fcmTokens)
            .putData("title", "User joined")
            .putData("message", "message")
            .build()
        //TODO deeplink
        //TODO message type
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