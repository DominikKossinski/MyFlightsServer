package pl.kossa.myflightsserver.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toSet
import pl.kossa.myflightsserver.data.models.SharingSettings
import pl.kossa.myflightsserver.repositories.SharingSettingsRepository

class SharingSettingsRepositoryMock : SharingSettingsRepository {

    private val sharingSettings = arrayListOf<SharingSettings>()

    override suspend fun findByUserId(userId: String): SharingSettings? {
        return sharingSettings.find { it.userId == userId }
    }

    override suspend fun count(): Long = sharingSettings.size.toLong()

    override suspend fun delete(entity: SharingSettings) {
        val settings = sharingSettings.find { it.userId == entity.userId }
        settings?.let { sharingSettings.remove(it) }
    }

    override suspend fun deleteAll() {
        sharingSettings.clear()
    }

    override suspend fun deleteAll(entities: Iterable<SharingSettings>) {
        for (entity in entities) {
            delete(entity)
        }
    }

    override suspend fun <S : SharingSettings> deleteAll(entityStream: Flow<S>) {
        entityStream.onEach {
            delete(it)
        }
    }

    override suspend fun deleteAllById(ids: Iterable<String>) {
        deleteAll(sharingSettings.filter { it.userId in ids })
    }

    override suspend fun deleteById(id: String) {
        val settings = sharingSettings.find { it.userId == id }
        settings?.let { delete(it) }
    }

    override suspend fun existsById(id: String): Boolean {
        return sharingSettings.find { it.userId == id } != null
    }

    override fun findAll(): Flow<SharingSettings> {
        return flow {
            sharingSettings
        }
    }

    override fun findAllById(ids: Iterable<String>): Flow<SharingSettings> {
        return flow { findAllById(ids) }
    }

    override fun findAllById(ids: Flow<String>): Flow<SharingSettings> {
        return flow { findAllById(ids.toSet()) }
    }

    override suspend fun findById(id: String): SharingSettings? {
        return sharingSettings.find { it.userId == id }
    }

    override suspend fun <S : SharingSettings> save(entity: S): SharingSettings {
        (entity as? SharingSettings)?.let {
            sharingSettings.find { it.userId == entity.userId }?.let { found ->
                sharingSettings.remove(found)
            }
            sharingSettings.add(it)
        }
        return entity
    }

    override fun <S : SharingSettings> saveAll(entities: Iterable<S>): Flow<S> {
        entities.forEach { entity ->
            (entity as? SharingSettings)?.let {
                sharingSettings.find { s -> s.userId == entity.userId }?.let { found ->
                    sharingSettings.remove(found)
                }
                sharingSettings.add(it)
            }

        }
        return flow { entities }
    }

    override fun <S : SharingSettings> saveAll(entityStream: Flow<S>): Flow<S> {
        entityStream.onEach {
            save(it)
        }
        return entityStream
    }
}