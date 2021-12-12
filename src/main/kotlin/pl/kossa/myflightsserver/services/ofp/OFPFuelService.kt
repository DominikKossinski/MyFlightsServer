package pl.kossa.myflightsserver.services.ofp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.ofp.OFPFuel
import pl.kossa.myflightsserver.repositories.ofp.OFPFuelRepository

@Service("OFPFuelService")
class OFPFuelService {

    @Autowired
    private lateinit var repository: OFPFuelRepository

    suspend fun saveOFPFuel(ofpFuel: OFPFuel) = repository.save(ofpFuel)
}