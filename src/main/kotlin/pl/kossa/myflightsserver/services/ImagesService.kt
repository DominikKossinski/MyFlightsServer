package pl.kossa.myflightsserver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.data.models.Image
import pl.kossa.myflightsserver.repositories.ImagesRepository

@Service("ImagesService")
class ImagesService {

    @Autowired
    private lateinit var repository: ImagesRepository

    suspend fun saveImage(image: Image) = repository.save(image)

    suspend fun deleteByImageId(imageId: String) = repository.deleteById(imageId)

}
