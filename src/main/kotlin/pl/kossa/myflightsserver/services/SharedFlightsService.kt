package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.repositories.SharedFlightsRepository

@Service("SharedFlightsService")
class SharedFlightsService {

    @Autowired
    private lateinit var repository: SharedFlightsRepository

    suspend fun getSharedFlightByFlightId(userId: String, flightId: String) =
        repository.findByOwnerIdAndFlightIdAndIsConfirmed(userId, flightId, false)

    suspend fun save(sharedFlight: SharedFlight) = repository.save(sharedFlight)

    suspend fun getSharedFlightsByOwnerId(userId: String) = repository.findAllByOwnerId(userId)

    suspend fun getSharedFlightBySharedFlightId(sharedFlightId: String) =
        repository.findBySharedFlightId(sharedFlightId)

    suspend fun getSharedFlightByOwnerIdAndSharedFlightId(ownerId: String, sharedFlightId: String) =
        repository.findByOwnerIdAndSharedFlightId(ownerId, sharedFlightId)

    suspend fun deleteSharedFlightById(sharedFlightId: String) = repository.deleteById(sharedFlightId)

    suspend fun getSharedFlightBySharedUserIdAndSharedFlightId(userId: String, sharedFlightId: String) =
        repository.findBySharedUserIdAndSharedFlightId(userId, sharedFlightId)

    suspend fun getSharedFlightsByOwnerIdAndFlightId(ownerId: String, flightId: String) =
        repository.findAllByOwnerIdAndFlightId(ownerId, flightId)

    suspend fun getSharedFlightBySharedUserIdAndFlightId(sharedUserId: String, flightId: String): SharedFlight? =
        repository.findBySharedUserIdAndFlightId(sharedUserId, flightId)

    suspend fun getSharedFlightsBySharedUserId(sharedUserId: String) =
        repository.findAllBySharedUserId(sharedUserId)
}