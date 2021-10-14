package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.repositories.FlightsRepository

@Service("FlightsService")
class FlightsService {

    @Autowired
    private lateinit var repository: FlightsRepository

    suspend fun getFlightsByUserId(uid: String) = repository.findAllByUserId(uid)

    suspend fun getFlightById(uid: String, flightId: String) = repository.findFlightByUserIdAndFlightId(uid, flightId)
        ?: throw NotFoundException("Flight with id '$flightId' not found.")

    suspend fun saveFlight(flight: Flight) = repository.save(flight)

    suspend fun deleteFlightById(flightId: String) = repository.deleteById(flightId)

    suspend fun deleteAllByUserId(userId: String) = repository.deleteAllByUserId(userId)


}
