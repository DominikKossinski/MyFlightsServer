package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.repositories.AirplanesRepository

@Service("AirplanesService")
class AirplanesService {

    @Autowired
    private lateinit var repository: AirplanesRepository
}