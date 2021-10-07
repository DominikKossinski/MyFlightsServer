package pl.kossa.myflightsserver.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import pl.kossa.myflightsserver.data.models.Runway
import pl.kossa.myflightsserver.repositories.RunwaysRepository

class RunwaysRepositoryMock : RunwaysRepository {

    private val runways = arrayListOf<Runway>()

    override suspend fun getRunwayByUserIdAndRunwayId(userId: String, runwayId: String): Runway? {
        return runways.find { it.userId == userId && it.runwayId == runwayId }
    }

    override suspend fun count(): Long = runways.size.toLong()

    override suspend fun delete(entity: Runway) {
        runways.remove(entity)
    }

    override suspend fun deleteAll() {
        runways.clear()
    }

    override suspend fun deleteAll(entities: Iterable<Runway>) {
        runways.removeAll(entities)
    }

    override suspend fun <S : Runway> deleteAll(entityStream: Flow<S>) {
        runways.removeAll(entityStream.toList())
    }

    override suspend fun deleteAllById(ids: Iterable<String>) {
        val toDelete = runways.filter { it.runwayId in ids }
        runways.removeAll(toDelete)
    }

    override suspend fun deleteById(id: String) {
        runways.find { it.runwayId == id }?.let { runways.remove(it) }
    }

    override suspend fun existsById(id: String): Boolean {
        return runways.find { it.runwayId == id } != null
    }

    override fun findAll(): Flow<Runway> {
        return flow { runways }
    }

    override fun findAllById(ids: Iterable<String>): Flow<Runway> {
        return flow { findAllById(ids) }
    }

    override fun findAllById(ids: Flow<String>): Flow<Runway> {
        return flow { findAllById(ids.toList()) }
    }

    override suspend fun findById(id: String): Runway? {
        return runways.find { it.runwayId == id }
    }

    override suspend fun <S : Runway> save(entity: S): Runway {
        runways.find { it.runwayId == entity.runwayId }?.let { runways.remove(it) }
        runways.add(entity)
        return entity
    }

    override fun <S : Runway> saveAll(entities: Iterable<S>): Flow<S> {
        for (entity in entities) {
            runways.find { it.runwayId == entity.runwayId }?.let { runways.remove(it) }
            runways.add(entity)

        }
        return flow { entities }
    }

    override fun <S : Runway> saveAll(entityStream: Flow<S>): Flow<S> {
        entityStream.onEach {
            save(it)
        }
        return entityStream
    }
}
