package pl.kossa.myflightsserver.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.repositories.AirportsRepository

class AirportsRepositoryMock : AirportsRepository {

    private val airports = arrayListOf<Airport>()

    override suspend fun findAirportsByUserId(userId: String): List<Airport> = airports.filter { it.userId == userId }

    override suspend fun findAirportByUserIdAndAirportId(userId: String, airportId: String): Airport? =
        airports.find { it.userId == userId && it.airportId == airportId }

    override suspend fun deleteAll(entities: Iterable<Airport>) {
        airports.removeAll(entities)
    }

    override suspend fun <S : Airport> deleteAll(entityStream: Flow<S>) {
        airports.removeAll(entityStream.toList())
    }

    override suspend fun deleteAllById(ids: Iterable<String>) {
        deleteAll(airports.filter { it.airportId in ids })
    }

    override suspend fun deleteById(id: String) {
        deleteAll(airports.filter { it.airportId == id })
    }

    override suspend fun existsById(id: String): Boolean {
        return airports.find { it.airportId == id } != null
    }

    override fun findAll(): Flow<Airport> = flow { airports }

    override fun findAllById(ids: Iterable<String>): Flow<Airport> = flow { airports.filter { it.airportId in ids } }

    override fun findAllById(ids: Flow<String>): Flow<Airport> =
        flow { airports.filter { it.airportId in ids.toList() } }

    override suspend fun findById(id: String): Airport? = airports.find { it.airportId == id }

    override suspend fun <S : Airport> save(entity: S): Airport {
        (entity as? Airport)?.let {
            airports.find { it.airportId == entity.airportId }?.let { found ->
                airports.remove(found)
            }
            airports.add(it)
        }
        return entity
    }

    override fun <S : Airport> saveAll(entities: Iterable<S>): Flow<S> {
        entities.forEach { entity ->
            (entity as? Airport)?.let {
                airports.find { it.airportId == entity.airportId }?.let { found ->
                    airports.remove(found)
                }
                airports.add(it)
            }
        }
        return flow { entities }
    }

    override fun <S : Airport> saveAll(entityStream: Flow<S>): Flow<S> {
        entityStream.onEach {
            save(it)
        }
        return entityStream
    }

    override suspend fun count(): Long = airports.size.toLong()

    override suspend fun delete(entity: Airport) {
        airports.remove(entity)
    }

    override suspend fun deleteAll() {
        airports.clear()
    }

}
