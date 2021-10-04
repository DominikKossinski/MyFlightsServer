package pl.kossa.myflightsserver.restcontrollers

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageOptions
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.imgscalr.Scalr
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
import java.io.ByteArrayOutputStream
import java.lang.System.getenv
import java.util.*
import javax.imageio.ImageIO


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
    suspend fun postImage(@RequestBody image: MultipartFile): ResponseEntity<Image> {
        val user = getUserDetails()
        val id = UUID.randomUUID().toString()
        val name = id + StringUtils.getFilenameExtension(image.originalFilename)
        val thumbnail = makeThumbnail(image)
        val thumbnailName = name + "thumbnail"
        val url = "${getenv("IMAGE_URL")}${name}?alt=media"
        val thumbnailUrl = "${getenv("IMAGE_URL")}${thumbnailName}?alt=media"
        createInFireStorage(name, image.bytes, image.contentType)
        createInFireStorage(thumbnailName, thumbnail, image.contentType)
        //TODO add max size to image
        val imageAdded = imagesService.saveImage(Image(id, url, thumbnailUrl, user.uid))
        return ResponseEntity.status(HttpStatus.CREATED).body(imageAdded)
    }

    private fun createInFireStorage(name: String, image: ByteArray, contentType: String?) {
        val storage = storageOptions.service
        val blobId = BlobId.of(getenv("FIRESTORAGE_NAME"), name)
        val blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build()
        storage.create(blobInfo, image)
    }

    private fun makeThumbnail(image: MultipartFile): ByteArray {
        val img = ImageIO.read(image.inputStream)
        val thumbImg = Scalr.resize(img, Scalr.Mode.AUTOMATIC, 100, Scalr.OP_ANTIALIAS)
        val thumbOutput = ByteArrayOutputStream()
        ImageIO.write(thumbImg, image.contentType!!.split("/")[1], thumbOutput)
        return thumbOutput.toByteArray()
    }

    //TODO put, delete

}
