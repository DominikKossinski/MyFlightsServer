package pl.kossa.myflightsserver.repositories.ofp

import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.ofp.OFPAircraft

interface OFPAircraftRepository : CrudRepository<OFPAircraft, String> {
}