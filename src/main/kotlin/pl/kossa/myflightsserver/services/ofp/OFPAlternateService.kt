package pl.kossa.myflightsserver.services.ofp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFPAlternate
import pl.kossa.myflightsserver.repositories.ofp.OFPAlternateRepository

@Service("OFPAlternateService")
class OFPAlternateService {

    @Autowired
    private lateinit var repository: OFPAlternateRepository

    suspend fun saveOFPAlternate(ofpAlternate: OFPAlternate) = repository.save(ofpAlternate)
}