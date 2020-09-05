package pl.kossa.myflightsserver.datasource

import pl.kossa.myflightsserver.data.models.User
import pl.kossa.myflightsserver.repositories.UsersRepository
import java.util.*

class UsersRepositoryMock : UsersRepository {

    private val users = arrayListOf(User("1", "Test", "test@test.pl", null))

    override fun findOneByEmail(email: String): User? = users.find { it.email == email }

    override fun <S : User?> save(entity: S): S {
        (entity as? User)?.let {
            val found = users.find { it.userId == entity.userId }
            if (found != null) users.remove(found)
            users.add(it)
        }
        return entity
    }

    override fun <S : User?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        entities.forEach { entity ->
            (entity as? User)?.let {
                val found = users.find { it.userId == entity.userId }
                if (found != null) users.remove(found)
                users.add(it)
            }
        }
        return entities
    }

    override fun findById(id: String): Optional<User> {
        val user = users.find { it.userId == id }
        if (user != null) {
            return Optional.of(user)
        }
        return Optional.empty()
    }

    override fun existsById(id: String): Boolean = users.find { it.userId == id } != null

    override fun findAll(): MutableIterable<User> = users

    override fun findAllById(ids: MutableIterable<String>): MutableIterable<User> = users.filter { ids.contains(it.userId) }.toMutableList()

    override fun count(): Long = users.size.toLong()

    override fun deleteById(id: String) {
        val user = users.find { it.userId == id }
        if (user != null) {
            users.remove(user)
        }
    }

    override fun delete(entity: User) {
        users.remove(entity)
    }

    override fun deleteAll(entities: MutableIterable<User>) {
        entities.forEach {
            users.remove(it)
        }
    }

    override fun deleteAll() {
        users.clear()
    }

}