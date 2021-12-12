package pl.kossa.myflightsserver.services.ofp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFPAirport
import pl.kossa.myflightsserver.repositories.ofp.OfpAirportsRepository

@Service("OFPAirportsService")
class OFPAirportsService {

    @Autowired
    private lateinit var repository: OfpAirportsRepository

    suspend fun saveOFPAirport(ofpAirport: OFPAirport) = repository.save(ofpAirport)
}