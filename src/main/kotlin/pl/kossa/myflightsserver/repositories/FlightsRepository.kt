package pl.kossa.myflightsserver.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.Flight

interface FlightsRepository : CrudRepository<Flight, Int> {

    @Query("FROM Flight WHERE userId = ?1")
    fun findByUserId(uid: String): Iterable<Flight>

    fun findOneByFlightId(flightId: Int): Flight?
}