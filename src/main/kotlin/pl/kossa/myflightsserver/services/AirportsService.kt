package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.repositories.AirportsRepository

@Service("AirportsService")
class AirportsService {


    @Autowired
    private lateinit var repository: AirportsRepository

    fun getAirportsByUserId(uid: String) = repository.findByUserId(uid).toList()

    fun getAirportById(airportId: Int, uid: String) = repository.findOneByAirportId(airportId, uid)
            ?: throw NotFoundException("Airport with id '$airportId' not found.")

    fun saveAirport(airport: Airport) = repository.save(airport)

    fun deleteAirportById(airportId: Int) = repository.deleteById(airportId)
}