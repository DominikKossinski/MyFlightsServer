package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.SharingMode
import pl.kossa.myflightsserver.data.models.SharingSettings
import pl.kossa.myflightsserver.repositories.SharingSettingsRepository

@Service("SharingSettingsService")
class SharingSettingsService {

    @Autowired
    private lateinit var repository: SharingSettingsRepository

    suspend fun findByUserId(userId: String) = repository.findByUserId(userId) ?: repository.save(
        SharingSettings(
            userId,
            SharingMode.PRIVATE,
            SharingMode.PRIVATE,
            SharingMode.PRIVATE,
            SharingMode.PRIVATE
        )
    )

    suspend fun save(sharingSettings: SharingSettings) = repository.save(sharingSettings)

    suspend fun deleteByUserId(userId: String) = repository.deleteById(userId)
}