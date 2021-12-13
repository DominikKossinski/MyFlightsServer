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
import pl.kossa.myflightsserver.data.models.ofp.*
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
import java.util.*
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

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
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
    suspend fun getOFPs(): List<OFP> {
        val user = getUserDetails()
        return ofpsService.getOFPSByUserId(user.uid)
    }

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
        val newOFP = saveOFP(user.uid, ofp)
        logger.info("OFP: $ofp")
        return CreatedResponse(newOFP.ofpId)
    }

    private suspend fun saveOFP(userId: String, simbriefOFP: SimbriefOFP): OFP {
        val origin = ofpAirportsService.saveOFPAirport(simbriefOFP.origin.toOFPAirport())
        val destination = ofpAirportsService.saveOFPAirport(simbriefOFP.destination.toOFPAirport())
        ofpAirportsService.saveOFPAirport(simbriefOFP.alternate.airport.toOFPAirport())
        val alternate = ofpAlternateService.saveOFPAlternate(simbriefOFP.alternate.toOFPAlternate())
        val aircraft = ofpAircraftService.saveOFPAircraft(simbriefOFP.aircraft.toOFPAircraft())
        val navlog = arrayListOf<OFPFix>()
        for (fix in simbriefOFP.navlog) {
            val wayPoint = when (fix.type) {
                OFPWayPointType.WPT -> {
                    val simpleWayPoint = OFPSimpleWayPoint(fix.ident, fix.name, fix.posLat, fix.posLong)
                    ofpSimpleWayPointService.saveOFPSimpleWayPoint(simpleWayPoint)
                }
                OFPWayPointType.LTLG -> {
                    val ltgtWayPoint =
                        OFPLtgtWayPoint(UUID.randomUUID().toString(), fix.ident, fix.name, fix.posLat, fix.posLong)
                    ofpLtgtWayPointService.saveOFPLtgtWayPoint(ltgtWayPoint)
                }
                OFPWayPointType.APT -> {
                    val ofpAptWayPoint =
                        OFPAptWayPoint(UUID.randomUUID().toString(), fix.ident, fix.name, fix.posLat, fix.posLong)
                    ofpAptWayPointsService.saveOFPAptWayPoint(ofpAptWayPoint)
                }
            }
            val ofpFix = OFPFix(UUID.randomUUID().toString(), wayPoint, fix.viaAirway)
            navlog.add(ofpFixService.saveOFPFix(ofpFix))
        }
        val fuel = simbriefOFP.fuel.toOFPFuel(UUID.randomUUID().toString())
        ofpFuelRepository.save(fuel)
        val ofp = OFP(
            UUID.randomUUID().toString(),
            simbriefOFP.general.toOFPGeneral(UUID.randomUUID().toString()),
            origin,
            destination,
            alternate,
            navlog,
            aircraft,
            fuel,
            emptyList(), //TODO files list
            userId
        )
        return ofpsService.saveOFP(ofp)
    }

}