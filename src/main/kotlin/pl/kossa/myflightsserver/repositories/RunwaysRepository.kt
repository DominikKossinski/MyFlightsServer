package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.Runway

interface RunwaysRepository : CrudRepository<Runway, Int>