package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.domain.SourceFile
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.nio.file.Path

@Repository
interface SourceFileRepository : CrudRepository<SourceFile, String>{
    @Query("select s from #{#entityName} s where s.sourceDir = ?1 and s.relativePath = ?2")
    fun findBySourceDirAndRelativePath(sourceDir: SourceDir, relativePath: Path) : SourceFile?

    fun countAllBySourceDir(sourceDir: SourceDir): Int

    fun findAllBySourceDir(sourceDir: SourceDir): List<SourceFile>
}
