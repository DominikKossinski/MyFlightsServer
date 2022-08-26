package pl.kossa.myflightsserver.restcontrollers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.models.SharingSettings
import pl.kossa.myflightsserver.data.requests.SharingSettingsRequest
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import java.util.*

@RestController
@RequestMapping("/api/settings")
class SettingsRestController : BaseRestController() {

    @GetMapping("sharing", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSharingSettings(locale: Locale = Locale.US): SharingSettings {
        val user = getUserDetails(locale)
        return sharingSettingsService.findByUserId(user.uid)
    }

    @PutMapping("sharing", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = UnauthorizedError::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = ForbiddenError::class))]
            )
        ]
    )
    suspend fun putSharingSettings(
        @RequestBody sharingSettingsRequest: SharingSettingsRequest,
        locale: Locale = Locale.US
    ) {
        val user = getUserDetails(locale)
        val sharedSettings = SharingSettings(
            user.uid,
            sharingSettingsRequest.emailSharingMode,
            sharingSettingsRequest.flightHoursSharingMode,
            sharingSettingsRequest.favouriteAirplaneSharingMode,
            sharingSettingsRequest.favouriteAirportsSharingMode
        )
        sharingSettingsService.save(sharedSettings)
    }

}