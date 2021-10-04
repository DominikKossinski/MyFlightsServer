package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.Airport

interface AirportsRepository : CoroutineCrudRepository<Airport, String> {

    //    @Query("select a from Airport a where a.userId = ?1")
    suspend fun findAirportsByUserId(userId: String): List<Airport>

    //    @Query("select a from Airport a where a.userId = ?1 and a.airportId = ?2")
    suspend fun findAirportByUserIdAndAirportId(userId: String, airportId: String): Airport?
}
