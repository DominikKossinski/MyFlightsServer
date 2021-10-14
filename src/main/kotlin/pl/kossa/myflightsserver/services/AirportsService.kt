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

    suspend fun getAirportsByUserId(userId: String, filter: String) =
        repository.findAirportByUserIdAndIcaoCodeOrCity(userId, filter, filter)

    suspend fun getAirportById(userId: String, airportId: String) =
        repository.findAirportByUserIdAndAirportId(userId, airportId)
            ?: throw NotFoundException("Airport with id '$airportId' not found.")

    suspend fun saveAirport(airport: Airport) = repository.save(airport)

    suspend fun deleteAirportById(airportId: String) = repository.deleteById(airportId)

    suspend fun deleteAllByUserId(userId: String) = repository.deleteAllByUserId(userId)
}
