package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.SharedFlight

interface SharedFlightsRepository : CoroutineCrudRepository<SharedFlight, String> {

    suspend fun findByFlightIdAndUserIdAndIsConfirmed(
        flightId: String,
        userId: String,
        isConfirmed: Boolean
    ): SharedFlight?
}