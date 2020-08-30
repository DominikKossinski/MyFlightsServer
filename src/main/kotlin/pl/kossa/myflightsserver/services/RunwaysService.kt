package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.repositories.RunwaysRepository

@Service("RunwaysService")
class RunwaysService {

    @Autowired
    private lateinit var repository: RunwaysRepository
}