package pl.kossa.myflightsserver.repositories

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.Airport

interface AirportsRepository : CoroutineCrudRepository<Airport, String> {

    @Query(
        "{'userId' : ?0 , '\$or' : " +
                "[ {'icaoCode' : {'\$regex' : /?1/, '\$options': 'i'} }, {'city' : {'\$regex' : /?2/, '\$options': 'i'}}]}"
    )
    suspend fun findAirportByUserIdAndIcaoCodeOrCity(
        userId: String,
        icaoCode: String,
        city: String
    ): List<Airport>

    suspend fun findAirportByUserIdAndAirportId(userId: String, airportId: String): Airport?

    suspend fun deleteAllByUserId(userId: String)
}
