package pl.kossa.myflightsserver.services.ofp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFPLtgtWayPoint
import pl.kossa.myflightsserver.repositories.ofp.OFPLtgtWayPointsRepository

@Service("OFPLtgtWayPointsService")
class OFPLtgtWayPointsService {

    @Autowired
    private lateinit var repository: OFPLtgtWayPointsRepository

    suspend fun saveOFPLtgtWayPoint(ofpLtgtWayPoint: OFPLtgtWayPoint) = repository.save(ofpLtgtWayPoint)
}