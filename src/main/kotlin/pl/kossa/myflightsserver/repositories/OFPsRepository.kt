package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.ofp.OFP

interface OFPsRepository : CoroutineCrudRepository<OFP, String> {


    suspend fun findByUserIdAndOfpId(userId: String, ofpId: String): OFP?

    suspend fun findAllByUserId(userId: String): List<OFP>

}