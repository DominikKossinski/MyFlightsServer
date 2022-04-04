package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.SharedFlight

interface SharedFlightsRepository : CoroutineCrudRepository<SharedFlight, String> {

    suspend fun findByOwnerIdAndFlightIdAndIsConfirmed(
        ownerId: String,
        flightId: String,
        isConfirmed: Boolean
    ): SharedFlight?

    suspend fun findAllByOwnerId(ownerId: String): List<SharedFlight>

    suspend fun findByOwnerIdAndSharedFlightId(ownerId: String, sharedFlightId: String): SharedFlight?

    suspend fun findBySharedFlightId(sharedFlightId: String): SharedFlight?

    suspend fun findBySharedUserIdAndSharedFlightId(sharedUserId: String, sharedFlightId: String): SharedFlight?

    suspend fun findAllByOwnerIdAndFlightId(userId: String, flightId: String): List<SharedFlight>

    suspend fun findBySharedUserIdAndFlightId(sharedUserId: String, flightId: String): SharedFlight?

    suspend fun findAllBySharedUserIdAndIsConfirmed(sharedUserId: String, isConfirmed: Boolean): List<SharedFlight>

    suspend fun findAllByOwnerIdAndIsConfirmed(ownerId: String, isConfirmed: Boolean): List<SharedFlight>

    suspend fun getSharedFlightsBySharedUserIdAndFlightId(userId: String, flightId: String): SharedFlight?
}