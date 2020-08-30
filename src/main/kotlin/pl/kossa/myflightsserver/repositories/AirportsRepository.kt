package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.Airport

interface AirportsRepository : CrudRepository<Airport, Int>