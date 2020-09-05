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

    fun getFlightsByUserId(uid: String) = repository.findByUserId(uid).toList()

    fun getFlightById(flightId: Int, uid: String) = repository.findOneByFlightId(flightId, uid)
            ?: throw NotFoundException("Flight with id '$flightId' not found.")

    fun saveFlight(flight: Flight) = repository.save(flight)

    fun deleteFlightById(flightId: Int) = repository.deleteById(flightId)


}