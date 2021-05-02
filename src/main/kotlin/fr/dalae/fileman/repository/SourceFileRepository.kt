package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.SourceFile
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SourceFileRepository : CrudRepository<SourceFile, Long>
