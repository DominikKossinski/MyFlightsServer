package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.SharingSettings

interface SharingSettingsRepository : CoroutineCrudRepository<SharingSettings, String> {

    suspend fun findByUserId(userId: String): SharingSettings?
}