package pl.kossa.myflightsserver.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.Runway

interface RunwaysRepository : CrudRepository<Runway, Int> {

    fun findOneByRunwayId(runwayId: Int): Runway?

    @Query("FROM Runway WHERE airport.airportId = ?1")
    fun findByAirportId(airportId: Int): Iterable<Runway>
}