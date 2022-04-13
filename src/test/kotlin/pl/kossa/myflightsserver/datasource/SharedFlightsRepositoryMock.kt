package pl.kossa.myflightsserver.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toSet
import pl.kossa.myflightsserver.data.models.SharedFlight
import pl.kossa.myflightsserver.repositories.SharedFlightsRepository

class SharedFlightsRepositoryMock : SharedFlightsRepository {

    private val sharedFlights = arrayListOf<SharedFlight>()

    override suspend fun findByOwnerIdAndFlightIdAndIsConfirmed(
        ownerId: String,
        flightId: String,
        isConfirmed: Boolean
    ): SharedFlight? =
        sharedFlights.find { it.ownerId == ownerId && it.flightId == flightId && it.isConfirmed == isConfirmed }


    override suspend fun findAllByOwnerId(ownerId: String): List<SharedFlight> {
        return sharedFlights.filter { it.ownerId == ownerId }
    }

    override suspend fun findByOwnerIdAndSharedFlightId(ownerId: String, sharedFlightId: String): SharedFlight? {
        return sharedFlights.find { it.ownerId == ownerId && it.sharedFlightId == sharedFlightId }
    }

    override suspend fun findBySharedFlightId(sharedFlightId: String): SharedFlight? {
        return sharedFlights.find { it.sharedFlightId == sharedFlightId }
    }

    override suspend fun findBySharedUserIdAndSharedFlightId(
        sharedUserId: String,
        sharedFlightId: String
    ): SharedFlight? {
        return sharedFlights.find { it.sharedUserId == sharedUserId && it.sharedFlightId == sharedFlightId }
    }

    override suspend fun findAllByOwnerIdAndFlightId(userId: String, flightId: String): List<SharedFlight> {
        return sharedFlights.filter { it.ownerId == userId && it.flightId == flightId }
    }

    override suspend fun findBySharedUserIdAndFlightIdAndIsConfirmed(
        sharedUserId: String,
        flightId: String,
        isConfirmed: Boolean
    ): SharedFlight? {
        return sharedFlights.find { it.sharedUserId == sharedUserId && it.flightId == flightId && it.isConfirmed == isConfirmed }
    }

    override suspend fun findAllBySharedUserIdAndIsConfirmed(
        sharedUserId: String,
        isConfirmed: Boolean
    ): List<SharedFlight> {
        return sharedFlights.filter { it.sharedUserId == sharedUserId && it.isConfirmed == isConfirmed }
    }

    override suspend fun findAllByOwnerIdAndIsConfirmed(ownerId: String, isConfirmed: Boolean): List<SharedFlight> {
        return sharedFlights.filter { it.ownerId == ownerId && it.isConfirmed == isConfirmed }
    }

    override suspend fun getSharedFlightsBySharedUserIdAndFlightId(userId: String, flightId: String): SharedFlight? {
        return sharedFlights.find { it.ownerId == userId && it.flightId == flightId }
    }

    override suspend fun count(): Long {
        return sharedFlights.size.toLong()
    }

    override suspend fun delete(entity: SharedFlight) {
        sharedFlights.remove(entity)
    }

    override suspend fun deleteAll() {
        sharedFlights.clear()
    }

    override suspend fun deleteAll(entities: Iterable<SharedFlight>) {
        sharedFlights.removeAll(entities.toSet())
    }

    override suspend fun <S : SharedFlight> deleteAll(entityStream: Flow<S>) {
        sharedFlights.removeAll(entityStream.toSet())
    }

    override suspend fun deleteAllById(ids: Iterable<String>) {
        deleteAll(sharedFlights.filter { it.sharedFlightId in ids })
    }

    override suspend fun deleteById(id: String) {
        deleteAll(sharedFlights.filter { it.sharedFlightId == id })
    }

    override suspend fun existsById(id: String): Boolean {
        return sharedFlights.find { it.sharedFlightId == id } != null
    }

    override fun findAll(): Flow<SharedFlight> = flow {
        sharedFlights
    }

    override fun findAllById(ids: Iterable<String>): Flow<SharedFlight> = flow {
        findAllById(ids)
    }

    override fun findAllById(ids: Flow<String>): Flow<SharedFlight> = flow {
        findAllById(ids.toSet())
    }

    override suspend fun findById(id: String): SharedFlight? {
        return sharedFlights.find { it.sharedFlightId == id }
    }

    override suspend fun <S : SharedFlight> save(entity: S): SharedFlight {
        (entity as? SharedFlight)?.let {
            sharedFlights.find { it.sharedFlightId == entity.sharedFlightId }?.let { found ->
                sharedFlights.remove(found)
            }
            sharedFlights.add(it)
        }
        return entity
    }

    override fun <S : SharedFlight> saveAll(entities: Iterable<S>): Flow<S> {
        entities.forEach { entity ->
            (entity as? SharedFlight)?.let { sharedFlight ->
                sharedFlights.find { it.sharedFlightId == sharedFlight.sharedFlightId }?.let { found ->
                    sharedFlights.remove(found)
                }
                sharedFlights.add(entity)
            }
        }
        return flow { entities }
    }

    override fun <S : SharedFlight> saveAll(entityStream: Flow<S>): Flow<S> {
        entityStream.onEach {
            save(it)
        }
        return entityStream
    }
}