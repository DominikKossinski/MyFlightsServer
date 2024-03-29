package pl.kossa.myflightsserver.datasource

import kotlinx.coroutines.flow.*
import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.repositories.AirplanesRepository

class AirplanesRepositoryMock : AirplanesRepository {

    private val airplanes = arrayListOf<Airplane>()

    override suspend fun findAirplaneByUserIdAndNameContainingIgnoreCase(userId: String, name: String): List<Airplane> {
        return airplanes.filter { it.userId == userId && it.name.lowercase().contains(name.lowercase()) }
    }


    override suspend fun findAirplaneByUserIdAndAirplaneId(userId: String, airplaneId: String): Airplane? =
        airplanes.find { it.userId == userId && it.airplaneId == airplaneId }

    override suspend fun deleteAllByUserId(userId: String) {
        val toDelete = airplanes.filter { it.userId == userId }
        airplanes.removeAll(toDelete.toSet())
    }

    override suspend fun deleteAll(entities: Iterable<Airplane>) {
        airplanes.removeAll(entities.toSet())
    }

    override suspend fun <S : Airplane> deleteAll(entityStream: Flow<S>) {
        airplanes.removeAll(entityStream.toSet())
    }

    override suspend fun deleteAllById(ids: Iterable<String>) {
        deleteAll(airplanes.filter { it.airplaneId in ids })
    }

    override suspend fun deleteById(id: String) {
        deleteAll(airplanes.filter { it.airplaneId == id })
    }

    override suspend fun existsById(id: String): Boolean {
        return airplanes.find { it.airplaneId == id } != null
    }

    override fun findAll(): Flow<Airplane> = flow { airplanes }

    override fun findAllById(ids: Iterable<String>): Flow<Airplane> = flow { airplanes.filter { it.airplaneId in ids } }

    override fun findAllById(ids: Flow<String>): Flow<Airplane> =
        flow { airplanes.filter { it.airplaneId in ids.toList() } }

    override suspend fun findById(id: String): Airplane? = airplanes.find { it.airplaneId == id }

    override suspend fun <S : Airplane> save(entity: S): Airplane {
        (entity as? Airplane)?.let {
            airplanes.find { it.airplaneId == entity.airplaneId }?.let { found ->
                airplanes.remove(found)
            }
            airplanes.add(it)
        }
        return entity
    }

    override fun <S : Airplane> saveAll(entities: Iterable<S>): Flow<S> {
        entities.forEach { entity ->
            (entity as? Airplane)?.let {
                airplanes.find { it.airplaneId == entity.airplaneId }?.let { found ->
                    airplanes.remove(found)
                }
                airplanes.add(it)
            }
        }
        return flow { entities }
    }

    override fun <S : Airplane> saveAll(entityStream: Flow<S>): Flow<S> {
        entityStream.onEach {
            save(it)
        }
        return entityStream
    }

    override suspend fun count(): Long = airplanes.size.toLong()

    override suspend fun delete(entity: Airplane) {
        airplanes.remove(entity)
    }

    override suspend fun deleteAll() {
        airplanes.clear()
    }
}
