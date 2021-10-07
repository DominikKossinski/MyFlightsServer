package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.Runway

interface RunwaysRepository : CoroutineCrudRepository<Runway, String> {

    suspend fun getRunwayByUserIdAndRunwayId(userId: String, runwayId: String): Runway?
}
