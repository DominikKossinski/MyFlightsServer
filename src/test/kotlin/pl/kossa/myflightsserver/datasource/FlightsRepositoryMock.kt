package pl.kossa.myflightsserver.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.repositories.FlightsRepository

class FlightsRepositoryMock : FlightsRepository {

    private val flights = arrayListOf<Flight>()

    override suspend fun findAllByUserId(userId: String): List<Flight> = flights.filter { it.userId == userId }

    override suspend fun findAllByUserIdAndIsPlanned(userId: String, isPlanned: Boolean): List<Flight> =
        flights.filter { it.userId == userId && it.isPlanned == isPlanned }

    override suspend fun findFlightByUserIdAndFlightId(userId: String, flightId: String): Flight? =
        flights.find { it.userId == userId && it.flightId == flightId }

    override suspend fun deleteAllByUserId(userId: String) {
        val toDelete = flights.filter { it.userId == userId }
        flights.removeAll(toDelete)
    }

    override suspend fun deleteAll(entities: Iterable<Flight>) {
        flights.removeAll(entities)
    }


    override suspend fun <S : Flight> deleteAll(entityStream: Flow<S>) {
        flights.removeAll(entityStream.toList())
    }

    override suspend fun deleteAllById(ids: Iterable<String>) {
        deleteAll(flights.filter { it.flightId in ids })
    }

    override suspend fun deleteById(id: String) {
        deleteAll(flights.filter { it.flightId == id })
    }

    override suspend fun existsById(id: String): Boolean {
        return flights.find { it.flightId == id } != null
    }

    override fun findAll(): Flow<Flight> = flow { flights }

    override fun findAllById(ids: Iterable<String>): Flow<Flight> = flow { flights.filter { it.flightId in ids } }

    override fun findAllById(ids: Flow<String>): Flow<Flight> =
        flow { flights.filter { it.flightId in ids.toList() } }

    override suspend fun findById(id: String): Flight? = flights.find { it.flightId == id }

    override suspend fun <S : Flight> save(entity: S): Flight {
        (entity as? Flight)?.let {
            flights.find { it.flightId == entity.flightId }?.let { found ->
                flights.remove(found)
            }
            flights.add(it)
        }
        return entity
    }

    override fun <S : Flight> saveAll(entities: Iterable<S>): Flow<S> {
        entities.forEach { entity ->
            (entity as? Flight)?.let {
                flights.find { it.flightId == entity.flightId }?.let { found ->
                    flights.remove(found)
                }
                flights.add(it)
            }
        }
        return flow { entities }
    }

    override fun <S : Flight> saveAll(entityStream: Flow<S>): Flow<S> {
        entityStream.onEach {
            save(it)
        }
        return entityStream
    }

    override suspend fun count(): Long = flights.size.toLong()

    override suspend fun delete(entity: Flight) {
        flights.remove(entity)
    }

    override suspend fun deleteAll() {
        flights.clear()
    }

}
