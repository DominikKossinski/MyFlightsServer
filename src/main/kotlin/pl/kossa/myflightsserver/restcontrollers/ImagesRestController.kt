package pl.kossa.myflightsserver.restcontrollers

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageOptions
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.models.Image
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import pl.kossa.myflightsserver.services.ImagesService
import java.lang.System.getenv
import java.util.*


@RestController
@RequestMapping("/api/images")
class ImagesRestController : BaseRestController() {

    @Autowired
    lateinit var storageOptions: StorageOptions

    @Autowired
    lateinit var imagesService: ImagesService

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
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
    fun postImage(@RequestBody image: MultipartFile): ResponseEntity<Image> {
        val user = getUserDetails()
        val id = UUID.randomUUID().toString()
        val name = id + StringUtils.getFilenameExtension(image.originalFilename)
        val url = "${getenv("IMAGE_URL")}${name}?alt=media"
        val storage = storageOptions.service
        val blobId = BlobId.of(getenv("FIRESTORAGE_NAME"), name) // TODO to env variable
        val blobInfo = BlobInfo.newBuilder(blobId).setContentType(image.contentType).build()
        storage.create(blobInfo, image.bytes)
        val imageAdded = imagesService.saveImage(Image(id, url, ""))
        // TODO post image to db and return image id
        // TODO reduce image size
        return ResponseEntity.status(HttpStatus.CREATED).body(imageAdded)
    }

    private fun generateFileName(originalFileName: String?): String {
        return UUID.randomUUID().toString() + StringUtils.getFilenameExtension(originalFileName)
    }
}