package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFP
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.repositories.OFPsRepository

@Service("OFPsService")
class OFPsService {

    @Autowired
    private lateinit var repository: OFPsRepository

    suspend fun saveOFP(ofp: OFP) = repository.save(ofp)

    suspend fun getOFPById(userId: String, ofpId: String) = repository.findByUserIdAndOfpId(userId, ofpId)
        ?: throw NotFoundException("OFP with id '$ofpId' not found.")

    suspend fun getOFPSByUserId(userId: String) = repository.findAllByUserId(userId)

}