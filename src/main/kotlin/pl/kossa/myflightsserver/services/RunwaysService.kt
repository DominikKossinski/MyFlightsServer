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


    fun getRunwayById(runwayId: Int) = repository.findOneByRunwayId(runwayId)
            ?: throw NotFoundException("Runway with id '$runwayId' not found.")

    fun getRunwaysByAirportId(airportId: Int) = repository.findByAirportId(airportId).toList()

    fun saveRunway(runway: Runway) = repository.save(runway)

    fun deleteByRunwayId(runwayId: Int) = repository.deleteById(runwayId)
}