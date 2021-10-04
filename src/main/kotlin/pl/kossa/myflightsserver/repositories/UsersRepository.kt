package pl.kossa.myflightsserver.repositories

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.User

interface UsersRepository : CoroutineCrudRepository<User, String> {

    @Query("{'email': ?0}")
    suspend fun findByEmail(email: String): User?

}
