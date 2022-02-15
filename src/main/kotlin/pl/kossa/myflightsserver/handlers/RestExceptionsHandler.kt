package pl.kossa.myflightsserver.handlers

import com.google.firebase.auth.FirebaseAuthException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException
import pl.kossa.myflightsserver.config.ApplicationConfig
import pl.kossa.myflightsserver.errors.*
import pl.kossa.myflightsserver.exceptions.ExistingFlightsException
import pl.kossa.myflightsserver.exceptions.ForbiddenException
import pl.kossa.myflightsserver.exceptions.NotFoundException
import pl.kossa.myflightsserver.exceptions.UnauthorizedException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionsHandler {

    @Autowired
    lateinit var applicationConfig: ApplicationConfig

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(
        notFoundException: NotFoundException
    ): ResponseEntity<NotFoundError> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(NotFoundError(notFoundException.message ?: ""))
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(
        unauthorizedException: UnauthorizedException
    ): ResponseEntity<UnauthorizedError> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(UnauthorizedError())
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(
        forbiddenException: ForbiddenException
    ): ResponseEntity<ForbiddenError> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(ForbiddenError())
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleSizeLimitExceededException(
        sizeLimitExceededException: MaxUploadSizeExceededException
    ): ResponseEntity<SizeLimitExceededError> {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE.value())
            .body(SizeLimitExceededError(applicationConfig.size.toString()))
    }

    @ExceptionHandler(ExistingFlightsException::class)
    fun handleExistingFlightsException(
        existingFlightsException: ExistingFlightsException
    ): ResponseEntity<ExistingFlightsError> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
            .body(ExistingFlightsError(existingFlightsException.type, existingFlightsException.entityId))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        methodArgumentNotValidException: MethodArgumentNotValidException
    ): ResponseEntity<ApiError> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
            .body(object : ApiError() {
                override val message = methodArgumentNotValidException.message
                override val description = methodArgumentNotValidException.message
            })
    }

    @ExceptionHandler(FirebaseAuthException::class)
    fun handleFirebaseAuthException(
        firebaseAuthException: FirebaseAuthException
    ): ResponseEntity<ApiError> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(UnauthorizedError())
    }
}
