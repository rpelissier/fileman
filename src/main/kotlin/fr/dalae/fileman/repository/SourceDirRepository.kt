package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.entity.SourceDir
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.nio.file.Path

@Repository
interface SourceDirRepository : CrudRepository<SourceDir, String> {

    @Query("select s from #{#entityName} s where s.path = ?1")
    fun findByPath(path: Path): SourceDir?
}
