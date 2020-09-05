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

    fun getFlightById(flightId: Int) = repository.findOneByFlightId(flightId)
            ?: throw NotFoundException("Flight with id '$flightId' not found.")

    fun deleteFlightById(flightId: Int) = repository.deleteById(flightId)

    fun saveFlight(flight: Flight) = repository.save(flight)

}