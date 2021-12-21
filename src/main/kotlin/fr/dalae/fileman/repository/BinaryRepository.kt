package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.entity.Binary
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BinaryRepository : CrudRepository<Binary, String>{

    fun findByLastModifiedEpochMsAndSize(lastModifiedEpochMs: Long, size: Long) : List<Binary>
}
