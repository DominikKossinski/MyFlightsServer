package pl.kossa.myflightsserver.datasource

import pl.kossa.myflightsserver.data.models.Runway
import pl.kossa.myflightsserver.repositories.RunwaysRepository
import java.util.*

class RunwaysRepositoryMock : RunwaysRepository {

    private val runways = arrayListOf<Runway>()

    override fun findOneByRunwayId(runwayId: Int): Runway? = runways.find { it.runwayId == runwayId }

    override fun findByAirportId(airportId: Int): Iterable<Runway> = runways.filter { it.airport.airportId == airportId }

    override fun <S : Runway?> save(entity: S): S {
        (entity as? Runway)?.let { runway ->
            val found = runways.find { it.runwayId == runway.runwayId }
            if (found != null) runways.remove(found)
            runways.add(runway)
        }
        return entity
    }

    override fun <S : Runway?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        entities.forEach { entity ->
            (entity as? Runway)?.let { runway ->
                val found = runways.find { it.runwayId == runway.runwayId }
                if (found != null) runways.remove(found)
                runways.add(runway)
            }
        }
        return entities
    }

    override fun findAll(): MutableIterable<Runway> = runways

    override fun findAllById(ids: MutableIterable<Int>): MutableIterable<Runway> = runways.filter { ids.contains(it.runwayId) }.toMutableList()

    override fun count(): Long = runways.size.toLong()

    override fun delete(entity: Runway) {
        runways.remove(entity)
    }

    override fun deleteAll(entities: MutableIterable<Runway>) {
        entities.forEach {
            runways.remove(it)
        }
    }

    override fun deleteAll() {
        runways.clear()
    }

    override fun deleteById(id: Int) {
        val runway = runways.find { it.runwayId == id }
        runway?.let {
            runways.remove(it)
        }
    }

    override fun existsById(id: Int): Boolean = runways.find { it.runwayId == id } != null

    override fun findById(id: Int): Optional<Runway> {
        val runway = runways.find { it.runwayId == id }
        if (runway != null) {
            return Optional.of(runway)
        }
        return Optional.empty()
    }

}