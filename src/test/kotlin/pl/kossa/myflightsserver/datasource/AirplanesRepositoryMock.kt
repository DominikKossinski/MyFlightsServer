package pl.kossa.myflightsserver.datasource

import pl.kossa.myflightsserver.data.models.Airplane
import pl.kossa.myflightsserver.repositories.AirplanesRepository
import java.util.*

class AirplanesRepositoryMock : AirplanesRepository {

    private val airplanes = arrayListOf<Airplane>()

    override fun findByUserId(uid: String): Iterable<Airplane> = airplanes.filter { it.userId == uid }

    override fun findOneByAirplaneId(airplaneId: Int, uid: String): Airplane? = airplanes.find { it.airplaneId == airplaneId && it.userId == uid }

    override fun <S : Airplane?> save(entity: S): S {
        (entity as? Airplane)?.let {
            if (entity.airplaneId > 0) {

                val found = airplanes.find { it.airplaneId == entity.airplaneId }
                if (found != null) airplanes.remove(found)
            }
            airplanes.add(it.copy(airplaneId = airplanes.size + 1))
        }
        return entity
    }

    override fun <S : Airplane?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        entities.forEach {
            (it as? Airplane)?.let { airplane ->
                if (airplane.airplaneId > 0) {
                    val found = airplanes.find { it.airplaneId == airplane.airplaneId }
                    if (found != null) airplanes.remove(found)
                }
                airplanes.add(airplane.copy(airplaneId = airplanes.size + 1))
            }
        }
        return entities
    }

    override fun findAll(): MutableIterable<Airplane> = airplanes

    override fun findAllById(ids: MutableIterable<Int>): MutableIterable<Airplane> = airplanes.filter { ids.contains(it.airplaneId) }.toMutableList()

    override fun count(): Long = airplanes.size.toLong()

    override fun delete(entity: Airplane) {
        airplanes.remove(entity)
    }

    override fun deleteAll(entities: MutableIterable<Airplane>) {
        entities.forEach {
            airplanes.remove(it)
        }
    }

    override fun deleteAll() {
        airplanes.clear()
    }

    override fun deleteById(id: Int) {
        val airplane = airplanes.find { it.airplaneId == id }
        airplane?.let {
            airplanes.remove(it)
        }
    }

    override fun existsById(id: Int): Boolean {
        return airplanes.find { it.airplaneId == id } != null
    }

    override fun findById(id: Int): Optional<Airplane> {
        val airplane = airplanes.find { it.airplaneId == id }
        if (airplane != null) {
            return Optional.of(airplane)
        }
        return Optional.empty()
    }


}