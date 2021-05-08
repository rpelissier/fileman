package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.domain.SourceFile
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.nio.file.Path

@Repository
interface SourceFileRepository : CrudRepository<SourceFile, Long>{
    @Query("select sf from #{#entityName} sf where sf.source_dir_id = ?1 and sf.relative_path = ?2")
    fun findBySourceDirAndRelativePath(sourceDir: SourceDir, relativePath: Path) : SourceFile?
}
