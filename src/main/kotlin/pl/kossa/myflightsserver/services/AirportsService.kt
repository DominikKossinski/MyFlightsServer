package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.repositories.AirportsRepository

@Service("AirportsService")
class AirportsService {


    @Autowired
    private lateinit var repository: AirportsRepository
}