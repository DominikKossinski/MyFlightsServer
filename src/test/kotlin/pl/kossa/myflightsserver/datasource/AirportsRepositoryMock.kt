package pl.kossa.myflightsserver.datasource

import pl.kossa.myflightsserver.data.models.Airport
import pl.kossa.myflightsserver.repositories.AirportsRepository
import java.util.*

class AirportsRepositoryMock : AirportsRepository {

    private val airports = arrayListOf<Airport>()

    override fun findByUserId(uid: String): Iterable<Airport> = airports.filter { it.userId == uid }

    override fun findOneByAirportId(airportId: Int, uid: String): Airport? = airports.find { it.airportId == airportId && it.userId == uid }

    override fun <S : Airport?> save(entity: S): S {
        (entity as? Airport)?.let {
            val found = airports.find { it.airportId == entity.airportId }
            if (found != null) airports.remove(found)
            airports.add(it)
        }
        return entity
    }

    override fun <S : Airport?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        entities.forEach { entity ->
            (entity as? Airport)?.let {
                val found = airports.find { it.airportId == entity.airportId }
                if (found != null) airports.remove(found)
                airports.add(it)
            }
        }
        return entities
    }

    override fun findAll(): MutableIterable<Airport> = airports

    override fun findAllById(ids: MutableIterable<Int>): MutableIterable<Airport> = airports.filter { ids.contains(it.airportId) }.toMutableList()

    override fun count(): Long = airports.size.toLong()

    override fun delete(entity: Airport) {
        airports.remove(entity)
    }

    override fun deleteAll(entities: MutableIterable<Airport>) {
        entities.forEach {
            airports.remove(it)
        }
    }

    override fun deleteAll() {
        airports.clear()
    }

    override fun deleteById(id: Int) {
        val airport = airports.find { it.airportId == id }
        airport?.let {
            airports.remove(it)
        }
    }

    override fun existsById(id: Int): Boolean {
        return airports.find { it.airportId == id } != null
    }

    override fun findById(id: Int): Optional<Airport> {
        val airport = airports.find { it.airportId == id }
        if (airport != null) {
            return Optional.of(airport)
        }
        return Optional.empty()
    }

}