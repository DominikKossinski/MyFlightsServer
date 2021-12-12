package pl.kossa.myflightsserver.services.ofp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFPAircraft
import pl.kossa.myflightsserver.repositories.ofp.OFPAircraftRepository

@Service("OFPAircraftService")
class OFPAircraftService {

    @Autowired
    private lateinit var repository: OFPAircraftRepository

    suspend fun saveOFPAircraft(ofpAircraft: OFPAircraft) = repository.save(ofpAircraft)
}