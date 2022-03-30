package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.SharedFlight

interface SharedFlightsRepository : CoroutineCrudRepository<SharedFlight, String> {

    suspend fun findByUserIdAndFlightIdAndIsConfirmed(
        userId: String,
        flightId: String,
        isConfirmed: Boolean
    ): SharedFlight?

    suspend fun findAllByUserId(userId: String): List<SharedFlight>

    suspend fun findByUserIdAndSharedFlightId(userId: String, sharedFlightId: String): SharedFlight?

    fun findBySharedFlightId(sharedFlightId: String): SharedFlight?
}