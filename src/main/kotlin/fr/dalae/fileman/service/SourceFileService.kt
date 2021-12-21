package fr.dalae.fileman.service

import fr.dalae.fileman.domain.SourceFilesSummary
import fr.dalae.fileman.domain.entity.SourceDir
import fr.dalae.fileman.domain.entity.SourceFile
import fr.dalae.fileman.repository.SourceDirRepository
import fr.dalae.fileman.repository.SourceFileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Service
class SourceFileService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var sourceFileRepository: SourceFileRepository

    @Autowired
    lateinit var sourceDirRepository: SourceDirRepository

    @Autowired
    lateinit var binaryService: BinaryService

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
    fun findAll(sourceDir: SourceDir): List<SourceFile> {
        return sourceFileRepository.findAll().toList()
    }

    @Transactional
    fun findAll(sourceDirPath: String): List<SourceFile> {
        val sourceDir = sourceDirRepository.findByPath(Path.of(sourceDirPath))?:return emptyList()
        return sourceFileRepository.findAllBySourceDir(sourceDir)
    }

    @Transactional
    fun countAll(sourceDir: SourceDir): Int {
        return sourceFileRepository.countAllBySourceDir(sourceDir)
    }

    @Transactional
    fun summarize(sourceDirPath: String): SourceFilesSummary {
        val sourceDir = sourceDirRepository.findByPath(Path.of(sourceDirPath))?:return SourceFilesSummary()
        val countAndSum = sourceFileRepository.getCountAndSumSize(sourceDir)[0]
        return SourceFilesSummary(countAndSum[0].toInt(), countAndSum[1].toLong())
    }
}