package fr.dalae.fileman.service

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.domain.SourceFile
import fr.dalae.fileman.repository.SourceFileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi

@Service
class SourceFileService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var sourceFileRepository: SourceFileRepository

    @Autowired
    lateinit var binaryService: BinaryService

    @OptIn(ExperimentalPathApi::class)
    @Transactional
    fun merge(sourceDir: SourceDir, relativePath: Path): SourceFile {

        log.debug("Merging path '${relativePath}'")
        val existingSourceFile =
            sourceFileRepository.findBySourceDirAndRelativePath(sourceDir, relativePath)

        val currentSourceFile = SourceFile.create(sourceDir, relativePath)

        if (existingSourceFile == null)
            return sourceFileRepository.save(currentSourceFile)

        val hasChanged = existingSourceFile.size != currentSourceFile.size
                || existingSourceFile.lastModifiedEpochMs != currentSourceFile.lastModifiedEpochMs

        if (hasChanged) {
            existingSourceFile.size = currentSourceFile.size
            existingSourceFile.lastModifiedEpochMs = currentSourceFile.lastModifiedEpochMs
        }

        return existingSourceFile
    }

    @Transactional
    fun findAll(sourceDir: SourceDir) : Sequence<SourceFile> {
        return sourceFileRepository.findAll().asSequence()
    }
}