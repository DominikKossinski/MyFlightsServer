package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("FlightsService")
class FlightsService {

    @Autowired
    private lateinit var service: FlightsService
}