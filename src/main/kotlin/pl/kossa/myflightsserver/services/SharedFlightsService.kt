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
        repository.findByUserIdAndFlightIdAndIsConfirmed(userId, flightId, false)

    suspend fun save(sharedFlight: SharedFlight) = repository.save(sharedFlight)

    suspend fun getSharedFlightsByUserId(userId: String) = repository.findAllByUserId(userId)

    suspend fun getSharedFlightBySharedFlightId(sharedFlightId: String) =
        repository.findBySharedFlightId(sharedFlightId)

    suspend fun getSharedFlightByUserIdAndSharedFlightId(userId: String, sharedFlightId: String) =
        repository.findByUserIdAndSharedFlightId(userId, sharedFlightId)
}