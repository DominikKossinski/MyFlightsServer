package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.Airplane

interface AirplanesRepository : CoroutineCrudRepository<Airplane, String> {

    suspend fun findAirplanesByUserId(userId: String): List<Airplane>

    suspend fun findAirplaneByUserIdAndAirplaneId(userId: String, airplaneId: String): Airplane?

}
