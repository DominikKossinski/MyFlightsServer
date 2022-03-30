package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.repositories.SharedFlightsRepository

@Service("SharedFlightsService")
class SharedFlightsService {

    @Autowired
    private lateinit var repository: SharedFlightsRepository

    suspend fun getSharedFlightByFlightId(userId: String, flightId: String) =
        repository.findByFlightIdAndUserIdAndIsConfirmed(flightId, userId, false)

    suspend fun save(sharedFlight: SharedFlight): SharedFlight = repository.save(sharedFlight)
}