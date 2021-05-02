package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.SourceDir
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SourceDirRepository : CrudRepository<SourceDir, Long>
