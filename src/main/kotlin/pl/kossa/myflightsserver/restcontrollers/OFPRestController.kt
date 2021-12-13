package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.models.ofp.OFP
import pl.kossa.myflightsserver.data.requests.OFPPostRequest
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.extensions.md5Simbrief
import pl.kossa.myflightsserver.repositories.ofp.OFPFuelRepository
import pl.kossa.myflightsserver.retrofit.models.SimbriefOFP
import pl.kossa.myflightsserver.retrofit.services.SimbriefService
import pl.kossa.myflightsserver.services.OFPsService
import pl.kossa.myflightsserver.services.ofp.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/ofps")
class OFPRestController : BaseRestController() {

    @Autowired
    private lateinit var ofpsService: OFPsService

    @Autowired
    private lateinit var ofpAirportsService: OFPAirportsService

    @Autowired
    private lateinit var ofpAlternateService: OFPAlternateService

    @Autowired
    private lateinit var ofpAircraftService: OFPAircraftService

    @Autowired
    private lateinit var ofpFixService: OFPFixService

    @Autowired
    private lateinit var ofpSimpleWayPointService: OFPSimpleWayPointsService

    @Autowired
    private lateinit var ofpLtgtWayPointService: OFPLtgtWayPointsService

    @Autowired
    private lateinit var ofpAptWayPointsService: OFPAptWayPointsService

    @Autowired
    private lateinit var ofpFuelRepository: OFPFuelRepository

    @Autowired
    private lateinit var simbriefService: SimbriefService

    //TODO
    @GetMapping("/{ofpId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = NotFoundError::class))]
            )
        ]
    )
    suspend fun getOFP(@PathVariable("ofpId") ofpId: String): OFP {
        val user = getUserDetails()
        return ofpsService.getOFPById(user.uid, ofpId)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(code = HttpStatus.CREATED)
    suspend fun postOFP(@RequestBody @Valid ofpPostRequest: OFPPostRequest): CreatedResponse {
        val user = getUserDetails()
        val md5 =
            md5Simbrief(ofpPostRequest.origin, ofpPostRequest.destination, ofpPostRequest.aircraft)
        logger.info("MD5: $md5")
        //TODO handle api exception
        val ofp = simbriefService.getOFP(
            ofpPostRequest.timestamp,
            md5
        ).body ?: throw NotFoundException("OFP not found")
//        val newOFP = saveOFP(ofp)
        logger.info("OFP: $ofp")
        return CreatedResponse("abc") //TODO
    }

    private fun saveOFP(simbriefOFP: SimbriefOFP) {
//        TODO("NOT IMPLEMENTED")
//        ofpAirportsService.saveOFPAirport(ofpRequest.destination)
//        ofpAirportsService.saveOFPAirport(ofpRequest.alternate.airport)
//        ofpAlternateService.saveOFPAlternate(ofpRequest.alternate)
//        ofpAircraftService.saveOFPAircraft(ofpRequest.aircraft)
//        val navlog = arrayListOf<OFPFix>()
//        for (fix in ofpRequest.navlog) {
//            val wayPoint = when (fix.type) {
//                OFPWayPointType.WPT -> {
//                    val simpleWayPoint = OFPSimpleWayPoint(fix.ident, fix.name, fix.posLat, fix.posLong)
//                    ofpSimpleWayPointService.saveOFPSimpleWayPoint(simpleWayPoint)
//                }
//                OFPWayPointType.LTGT -> {
//                    val ltgtWayPoint =
//                        OFPLtgtWayPoint(UUID.randomUUID().toString(), fix.ident, fix.name, fix.posLat, fix.posLong)
//                    ofpLtgtWayPointService.saveOFPLtgtWayPoint(ltgtWayPoint)
//                }
//                OFPWayPointType.APT -> {
//                    val ofpAptWayPoint =
//                        OFPAptWayPoint(UUID.randomUUID().toString(), fix.ident, fix.name, fix.posLat, fix.posLong)
//                    ofpAptWayPointsService.saveOFPAptWayPoint(ofpAptWayPoint)
//                }
//            }
//            val ofpFix = OFPFix(UUID.randomUUID().toString(), wayPoint, fix.viaAirway)
//            navlog.add(ofpFixService.saveOFPFix(ofpFix))
//        }
//        val fuel = ofpRequest.fuel.toFuel(UUID.randomUUID().toString())
//        ofpFuelRepository.save(fuel)
//        val ofp = OFP(
//            UUID.randomUUID().toString(),
//            ofpRequest.general.toGeneral(UUID.randomUUID().toString()),
//            ofpRequest.origin,
//            ofpRequest.destination,
//            ofpRequest.alternate,
//            navlog,
//            ofpRequest.aircraft,
//            fuel,
//            emptyList(), //TODO files list
//            user.uid
//        )
//        val newId = ofpsService.saveOFP(ofp).ofpId

    }
//    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
//    @ResponseStatus(code = HttpStatus.CREATED)
//    @ApiResponses(
//        value = [
//            ApiResponse(responseCode = "201"),
//            ApiResponse(
//                responseCode = "401",
//                description = "Unauthorized",
//                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
//            ),
//            ApiResponse(
//                responseCode = "403",
//                description = "Forbidden",
//                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
//            )
//        ]
//    )
//    suspend fun postOFP(@RequestBody @Valid ofpRequest: OFPRequest): CreatedResponse {
//        val user = getUserDetails()
//        ofpAirportsService.saveOFPAirport(ofpRequest.origin)
//        ofpAirportsService.saveOFPAirport(ofpRequest.destination)
//        ofpAirportsService.saveOFPAirport(ofpRequest.alternate.airport)
//        ofpAlternateService.saveOFPAlternate(ofpRequest.alternate)
//        ofpAircraftService.saveOFPAircraft(ofpRequest.aircraft)
//        val navlog = arrayListOf<OFPFix>()
//        for (fix in ofpRequest.navlog) {
//            val wayPoint = when (fix.type) {
//                OFPWayPointType.WPT -> {
//                    val simpleWayPoint = OFPSimpleWayPoint(fix.ident, fix.name, fix.posLat, fix.posLong)
//                    ofpSimpleWayPointService.saveOFPSimpleWayPoint(simpleWayPoint)
//                }
//                OFPWayPointType.LTGT -> {
//                    val ltgtWayPoint =
//                        OFPLtgtWayPoint(UUID.randomUUID().toString(), fix.ident, fix.name, fix.posLat, fix.posLong)
//                    ofpLtgtWayPointService.saveOFPLtgtWayPoint(ltgtWayPoint)
//                }
//                OFPWayPointType.APT -> {
//                    val ofpAptWayPoint =
//                        OFPAptWayPoint(UUID.randomUUID().toString(), fix.ident, fix.name, fix.posLat, fix.posLong)
//                    ofpAptWayPointsService.saveOFPAptWayPoint(ofpAptWayPoint)
//                }
//            }
//            val ofpFix = OFPFix(UUID.randomUUID().toString(), wayPoint, fix.viaAirway)
//            navlog.add(ofpFixService.saveOFPFix(ofpFix))
//        }
//        val fuel = ofpRequest.fuel.toFuel(UUID.randomUUID().toString())
//        ofpFuelRepository.save(fuel)
//        val ofp = OFP(
//            UUID.randomUUID().toString(),
//            ofpRequest.general.toGeneral(UUID.randomUUID().toString()),
//            ofpRequest.origin,
//            ofpRequest.destination,
//            ofpRequest.alternate,
//            navlog,
//            ofpRequest.aircraft,
//            fuel,
//            emptyList(), //TODO files list
//            user.uid
//        )
//        val newId = ofpsService.saveOFP(ofp).ofpId
//        return CreatedResponse(newId)
//    }

}