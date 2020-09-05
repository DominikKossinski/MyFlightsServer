package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.CrudRepository
import pl.kossa.myflightsserver.data.models.User

interface UsersRepository : CrudRepository<User, String> {

    fun findOneByEmail(email: String): User?

}