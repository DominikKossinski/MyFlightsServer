package pl.kossa.myflightsserver.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.Airport

interface AirportsRepository : CrudRepository<Airport, Int> {

    fun findByUserId(uid: String): Iterable<Airport>

    @Query("FROM Airport WHERE airportId = ?1 AND userId = ?2")
    fun findOneByAirportId(airportId: Int, uid: String): Airport?
}