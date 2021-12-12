package pl.kossa.myflightsserver.services.ofp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFPSimpleWayPoint
import pl.kossa.myflightsserver.repositories.ofp.OFPSimpleWayPointsRepository

@Service("OFPSimpleWayPointsService")
class OFPSimpleWayPointsService {

    @Autowired
    private lateinit var repository: OFPSimpleWayPointsRepository

    suspend fun saveOFPSimpleWayPoint(ofpSimpleWayPoint: OFPSimpleWayPoint) = repository.save(ofpSimpleWayPoint)
}