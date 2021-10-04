package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.User
import pl.kossa.myflightsserver.repositories.UsersRepository

@Service("UsersService")
class UsersService {

    @Autowired
    private lateinit var repository: UsersRepository


    suspend fun getUserByEmail(email: String) = repository.findByEmail(email)

    suspend fun saveUser(user: User) = repository.save(user)

}
