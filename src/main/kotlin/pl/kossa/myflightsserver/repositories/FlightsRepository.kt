package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.Flight

interface FlightsRepository : CrudRepository<Flight, Int>