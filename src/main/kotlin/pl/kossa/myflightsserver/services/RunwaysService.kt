package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.Runway
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.repositories.RunwaysRepository

@Service("RunwaysService")
class RunwaysService {

    @Autowired
    private lateinit var repository: RunwaysRepository

    suspend fun getRunwayById(userId: String, runwayId: String) =
        repository.getRunwayByUserIdAndRunwayId(userId, runwayId)
            ?: throw NotFoundException("Runway with id '$runwayId' not found.")

    suspend fun saveRunway(runway: Runway) = repository.save(runway)

    suspend fun deleteRunwayById(runwayId: String) = repository.deleteById(runwayId)

    suspend fun deleteAllByUserId(userId: String) = repository.deleteAllByUserId(userId)
}
