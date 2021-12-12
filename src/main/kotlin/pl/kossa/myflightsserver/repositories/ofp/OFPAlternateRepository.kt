package pl.kossa.myflightsserver.repositories.ofp

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.ofp.OFPAlternate

interface OFPAlternateRepository : CoroutineCrudRepository<OFPAlternate, String> {
}