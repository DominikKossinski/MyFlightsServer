package pl.kossa.myflightsserver.datasource

import pl.kossa.myflightsserver.data.models.Flight
import pl.kossa.myflightsserver.repositories.FlightsRepository
import java.util.*

class FlightsRepositoryMock : FlightsRepository {

    private val flights = arrayListOf<Flight>()

    override fun findByUserId(uid: String): Iterable<Flight> = flights.filter { it.userId == uid }

    override fun findOneByFlightId(flightId: Int, uid: String): Flight? = flights.find { it.flightId == flightId && it.userId == uid }

    override fun <S : Flight?> save(entity: S): S {
        (entity as? Flight)?.let { flight ->
            if (flight.flightId > 0) {
                val found = flights.find { it.flightId == flight.flightId }
                if (found != null) flights.remove(found)
            }
            flights.add(flight.copy(flightId = flights.size + 1))
        }
        return entity
    }

    override fun <S : Flight?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        entities.forEach { entity ->
            (entity as? Flight)?.let { flight ->
                if (flight.flightId > 0) {
                    val found = flights.find { it.flightId == flight.flightId }
                    if (found != null) flights.remove(found)
                }
                flights.add(flight.copy(flightId = flights.size + 1))
            }
        }
        return entities
    }

    override fun findAll(): MutableIterable<Flight> = flights

    override fun findAllById(ids: MutableIterable<Int>): MutableIterable<Flight> = flights.filter { ids.contains(it.flightId) }.toMutableList()

    override fun count(): Long = flights.size.toLong()

    override fun delete(entity: Flight) {
        flights.remove(entity)
    }

    override fun deleteAll(entities: MutableIterable<Flight>) {
        entities.forEach {
            flights.remove(it)
        }
    }

    override fun deleteAll() {
        flights.clear()
    }

    override fun deleteById(id: Int) {
        val flight = flights.find { it.flightId == id }
        flight?.let {
            flights.remove(it)
        }
    }

    override fun existsById(id: Int): Boolean = flights.find { it.flightId == id } != null

    override fun findById(id: Int): Optional<Flight> {
        val flight = flights.find { it.flightId == id }
        if (flight != null) return Optional.of(flight)
        return Optional.empty()
    }

}