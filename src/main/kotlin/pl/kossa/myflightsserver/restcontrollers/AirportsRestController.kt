package pl.kossa.myflightsserver.restcontrollers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.kossa.myflightsserver.architecture.BaseRestController

@RestController
@RequestMapping("/api/airports")
class AirportsRestController : BaseRestController() {

}