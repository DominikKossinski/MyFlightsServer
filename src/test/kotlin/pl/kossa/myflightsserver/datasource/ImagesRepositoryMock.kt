package pl.kossa.myflightsserver.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import pl.kossa.myflightsserver.data.models.Image
import pl.kossa.myflightsserver.repositories.ImagesRepository

class ImagesRepositoryMock : ImagesRepository {

    val images = arrayListOf<Image>()

    override suspend fun findByUserIdAndImageId(userId: String, imageId: String): Image? {
        return images.find { it.userId == userId && it.imageId == imageId }
    }

    override suspend fun deleteAllByUserId(userId: String) {
        images.removeAll(images.filter { it.userId == userId })
    }

    override suspend fun count(): Long = images.size.toLong()

    override suspend fun delete(entity: Image) {
        images.remove(entity)
    }

    override suspend fun deleteAll() {
        images.clear()
    }

    override suspend fun deleteAll(entities: Iterable<Image>) {
        images.removeAll(entities)
    }

    override suspend fun <S : Image> deleteAll(entityStream: Flow<S>) {
        entityStream.onEach {
            images.remove(it)
        }
    }

    override suspend fun deleteAllById(ids: Iterable<String>) {
        images.removeAll(images.filter { it.imageId in ids })
    }

    override suspend fun deleteById(id: String) {
        val image = images.find { it.imageId == id }
        image?.let { images.remove(it) }
    }

    override suspend fun existsById(id: String): Boolean {
        return images.find { it.imageId == id } != null
    }

    override fun findAll(): Flow<Image> {
        return flow { images }
    }

    override fun findAllById(ids: Iterable<String>): Flow<Image> {
        return flow { findAllById(ids) }
    }

    override fun findAllById(ids: Flow<String>): Flow<Image> {
        return flow { findAllById(ids.toList()) }
    }

    override suspend fun findById(id: String): Image? {
        return images.find { it.imageId == id }
    }

    override suspend fun <S : Image> save(entity: S): Image {
        images.find { it.imageId == entity.imageId }?.let { images.remove(it) }
        images.add(entity)
        return entity
    }

    override fun <S : Image> saveAll(entities: Iterable<S>): Flow<S> {
        entities.forEach { entity ->
            images.find { it.imageId == entity.imageId }?.let { images.remove(it) }
            images.add(entity)
        }
        return flow { entities }
    }

    override fun <S : Image> saveAll(entityStream: Flow<S>): Flow<S> {
        entityStream.onEach { entity ->
            images.find { it.imageId == entity.imageId }?.let { images.remove(it) }
            images.add(entity)
        }
        return entityStream
    }
}
