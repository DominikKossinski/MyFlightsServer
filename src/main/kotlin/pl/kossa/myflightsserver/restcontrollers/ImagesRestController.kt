package pl.kossa.myflightsserver.restcontrollers

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.imgscalr.Scalr
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.kossa.myflightsserver.architecture.BaseRestController
import pl.kossa.myflightsserver.data.models.Image
import pl.kossa.myflightsserver.data.responses.CreatedResponse
import pl.kossa.myflightsserver.errors.ForbiddenError
import pl.kossa.myflightsserver.errors.UnauthorizedError
import java.io.ByteArrayOutputStream
import java.lang.System.getenv
import java.util.*
import javax.imageio.ImageIO


@RestController
@RequestMapping("/api/images")
class ImagesRestController : BaseRestController() {

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
    suspend fun postImage(
        @RequestBody image: MultipartFile, locale: Locale
    ): ResponseEntity<CreatedResponse> {
        val user = getUserDetails(locale)
        val id = UUID.randomUUID().toString()
        val name = id + "." + StringUtils.getFilenameExtension(image.originalFilename)
        logger.info("File name $name")
        val thumbnail = makeThumbnail(image)
        val thumbnailName = id + "thumbnail." + StringUtils.getFilenameExtension(image.originalFilename)
        val url = "${getenv("IMAGE_URL")}${name}?alt=media"
        val thumbnailUrl = "${getenv("IMAGE_URL")}${thumbnailName}?alt=media"
        createInFireStorage(name, image.bytes, image.contentType)
        createInFireStorage(thumbnailName, thumbnail, image.contentType)
        //TODO add max size to image
        val imageAdded = imagesService.saveImage(Image(id, name, url, thumbnailUrl, user.uid))
        return ResponseEntity.status(HttpStatus.CREATED).body(CreatedResponse(imageAdded.imageId))
    }

    @PutMapping(
        "{imageId}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
    )
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
    suspend fun putImage(
        @PathVariable("imageId") imageId: String,
        @RequestBody image: MultipartFile,
        locale: Locale
    ) {
        val user = getUserDetails(locale)
        val imageDB = imagesService.getImageById(user.uid, imageId)
        val thumbnail = makeThumbnail(image)
        val thumbnailName = imageDB.firestoreName.replace(".", "thumbnail.")
        createInFireStorage(imageDB.firestoreName, image.bytes, image.contentType)
        createInFireStorage(thumbnailName, thumbnail, image.contentType)
    }

    private fun makeThumbnail(image: MultipartFile): ByteArray {
        val img = ImageIO.read(image.inputStream)
        val thumbImg = Scalr.resize(img, Scalr.Mode.AUTOMATIC, 100, Scalr.OP_ANTIALIAS)
        val thumbOutput = ByteArrayOutputStream()
        ImageIO.write(thumbImg, image.contentType!!.split("/")[1], thumbOutput)
        return thumbOutput.toByteArray()
    }

    protected fun createInFireStorage(name: String, image: ByteArray, contentType: String?) {
        val storage = storageOptions.service
        val blobId = BlobId.of(getenv("FIRESTORAGE_NAME"), name)
        val blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build()
        storage.create(blobInfo, image)
    }


}
