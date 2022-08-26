package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.responses.statistics.StatisticsResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.NotFoundError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.services.StatisticsService
import java.util.*

@RestController
@RequestMapping("/api/statistics")
class StatisticsRestController : BaseRestController() {

    @Autowired
    private lateinit var statisticsService: StatisticsService

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
    suspend fun getUserStats(locale: Locale = Locale.US): StatisticsResponse {
        val user = getUserDetails(locale)
        return statisticsService.getUserStatistics(user.uid)
    }
}