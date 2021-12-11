package fr.dalae.fileman.service

import fr.dalae.fileman.domain.SourceFile
import fr.dalae.fileman.repository.SourceFileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.io.path.ExperimentalPathApi

@Service
class SourceFileService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var sourceFileRepository: SourceFileRepository

    @Autowired
    lateinit var binaryService: BinaryService

    @OptIn(ExperimentalPathApi::class)
    fun merge(sourceFile: SourceFile): SourceFile {

        log.debug("Merging path '${sourceFile.relativePath}'")
        val existingSourceFile =
            sourceFileRepository.findBySourceDirAndRelativePath(sourceFile.sourceDir, sourceFile.relativePath)

        if (existingSourceFile == null) {
            return sourceFileRepository.save(sourceFile)
        }else{
            if (existingSourceFile.size != sourceFile.size || existingSourceFile.lastModifiedEpochMs != sourceFile.lastModifiedEpochMs) {
                log.info("Changed size or date.")
                //TODO Trigger something with binary service
            }
            return existingSourceFile
        }
    }
}