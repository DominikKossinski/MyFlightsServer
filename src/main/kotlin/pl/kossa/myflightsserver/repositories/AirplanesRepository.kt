package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.Airplane

interface AirplanesRepository : CrudRepository<Airplane, Int>