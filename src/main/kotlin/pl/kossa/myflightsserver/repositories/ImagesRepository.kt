package pl.kossa.myflightsserver.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import pl.kossa.myflightsserver.data.models.Image

interface ImagesRepository : CoroutineCrudRepository<Image, String> {

    suspend fun findByUserIdAndImageId(userId: String, imageId: String): Image?

    suspend fun deleteAllByUserId(userId: String)
}
