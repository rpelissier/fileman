package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.entity.SourceDir
import fr.dalae.fileman.domain.entity.SourceFile
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

    @Query("select count(s.size), sum(s.size) from #{#entityName} s where s.sourceDir = ?1")
    fun getCountAndSumSize(sourceDir: SourceDir): Array<Array<Number>>;
}
