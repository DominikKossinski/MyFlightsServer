package pl.kossa.myflightsserver.repositories.ofp

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.ofp.OFPSimpleWayPoint

interface OFPSimpleWayPointsRepository : CoroutineCrudRepository<OFPSimpleWayPoint, String> {
}