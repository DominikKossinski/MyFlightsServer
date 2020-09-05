package pl.kossa.myflightsserver.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.Airplane

interface AirplanesRepository : CrudRepository<Airplane, Int> {

    fun findByUserId(uid: String): Iterable<Airplane>

    @Query("FROM Airplane WHERE airplaneId = ?1 AND userId = ?2")
    fun findOneByAirplaneId(airplaneId: Int, uid: String): Airplane?
}