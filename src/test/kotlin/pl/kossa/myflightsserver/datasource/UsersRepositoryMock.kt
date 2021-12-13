package pl.kossa.myflightsserver.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import pl.kossa.myflightsserver.data.models.User
import pl.kossa.myflightsserver.repositories.UsersRepository

class UsersRepositoryMock : UsersRepository {

    private val users = arrayListOf(User("1", "Test", "test@test.pl", null, null))

    override suspend fun findByEmail(email: String): User? = users.find { it.email == email }

    override suspend fun deleteAll(entities: Iterable<User>) {
        users.removeAll(entities)
    }

    override suspend fun <S : User> deleteAll(entityStream: Flow<S>) {
        users.removeAll(entityStream.toList())
    }

    override suspend fun deleteAllById(ids: Iterable<String>) {
        deleteAll(users.filter { it.userId in ids })
    }

    override suspend fun deleteById(id: String) {
        deleteAll(users.filter { it.userId == id })
    }

    override suspend fun existsById(id: String): Boolean {
        return users.find { it.userId == id } != null
    }

    override fun findAll(): Flow<User> = flow { users }

    override fun findAllById(ids: Iterable<String>): Flow<User> = flow { users.filter { it.userId in ids } }

    override fun findAllById(ids: Flow<String>): Flow<User> =
        flow { users.filter { it.userId in ids.toList() } }

    override suspend fun findById(id: String): User? = users.find { it.userId == id }

    override suspend fun <S : User> save(entity: S): User {
        (entity as? User)?.let {
            users.find { it.userId == entity.userId }?.let { found ->
                users.remove(found)
            }
            users.add(it)
        }
        return entity
    }

    override fun <S : User> saveAll(entities: Iterable<S>): Flow<S> {
        entities.forEach { entity ->
            (entity as? User)?.let {
                users.find { it.userId == entity.userId }?.let { found ->
                    users.remove(found)
                }
                users.add(it)
            }
        }
        return flow { entities }
    }

    override fun <S : User> saveAll(entityStream: Flow<S>): Flow<S> {
        entityStream.onEach {
            save(it)
        }
        return entityStream
    }

    override suspend fun count(): Long = users.size.toLong()

    override suspend fun delete(entity: User) {
        users.remove(entity)
    }

    override suspend fun deleteAll() {
        users.clear()
    }

}
