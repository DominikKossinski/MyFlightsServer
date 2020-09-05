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

    fun getAirplanesByUserId(uid: String) = repository.findByUserId(uid).toList()

    fun getAirplaneById(airplaneId: Int, uid: String) = repository.findOneByAirplaneId(airplaneId, uid)
            ?: throw NotFoundException("Airplane with id '$airplaneId' not found.")

    fun saveAirplane(airplane: Airplane) = repository.save(airplane)

    fun deleteAirplaneById(airplaneId: Int) = repository.deleteById(airplaneId)
}