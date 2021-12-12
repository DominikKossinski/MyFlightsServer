package pl.kossa.myflightsserver.services.ofp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFPAptWayPoint
import pl.kossa.myflightsserver.repositories.ofp.OFPAptWayPointsRepository

@Service("OFPAptWayPointsService")
class OFPAptWayPointsService {

    @Autowired
    private lateinit var repository: OFPAptWayPointsRepository

    suspend fun saveOFPAptWayPoint(ofpAptWayPoint: OFPAptWayPoint) = repository.save(ofpAptWayPoint)
}
