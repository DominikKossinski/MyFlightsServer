package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.repositories.AirplanesRepository

@Service("AirplanesService")
class AirplanesService {

    @Autowired
    private lateinit var repository: AirplanesRepository

    suspend fun getAirplanesByUserId(userId: String, filter: String) =
        repository.findAirplaneByUserIdAndNameContainingIgnoreCase(userId, filter)

    suspend fun getAirplaneById(uid: String, airplaneId: String) =
        repository.findAirplaneByUserIdAndAirplaneId(uid, airplaneId)
            ?: throw NotFoundException("Airplane with id '$airplaneId' not found.")

    suspend fun saveAirplane(airplane: Airplane) = repository.save(airplane)

    suspend fun deleteAirplaneById(airplaneId: String) = repository.deleteById(airplaneId)

    suspend fun deleteAllByUserId(userId: String) = repository.deleteAllByUserId(userId)
}
