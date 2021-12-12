package pl.kossa.myflightsserver.services.ofp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFPFix
import pl.kossa.myflightsserver.repositories.ofp.OFPFixRepository

@Service("OFPFixService")
class OFPFixService {

    @Autowired
    private lateinit var repository: OFPFixRepository

    suspend fun saveOFPFix(ofpFix: OFPFix) = repository.save(ofpFix)
}