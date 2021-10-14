package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.Flight

interface FlightsRepository : CoroutineCrudRepository<Flight, String> {

    suspend fun findAllByUserId(userId: String): List<Flight>

    suspend fun findFlightByUserIdAndFlightId(userId: String, flightId: String): Flight?

    suspend fun deleteAllByUserId(userId: String)
}
