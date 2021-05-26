package fr.dalae.fileman

import fr.dalae.fileman.file.FileUtils
import fr.dalae.fileman.service.DocumentService
import fr.dalae.fileman.service.SourceDirService
import fr.dalae.fileman.service.SourceFileService
import java.nio.file.Path
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DirectoryLoader(config: ApplicationProperties) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var sourceDirService: SourceDirService

    @Autowired
    lateinit var documentService: DocumentService

    @Autowired
    lateinit var sourceFileService: SourceFileService

    val storageDir = Path.of(config.storageDir)
        .apply { toFile().mkdirs() }

    val batchSize = config.batchSize
    var observer = CountingObserver.loggingObserver(batchSize)

    @Transactional
    fun load(sourceDirPath: Path) {
        val sourceDir = sourceDirService.merge(sourceDirPath)
        val pathSequence = FileUtils.fileWalkingSequence(sourceDir.path)
        pathSequence
            .map {
                val document = documentService.merge(sourceDir, it)
                val sourceFile = sourceFileService.merge(sourceDir, it, document)
                observer.notifyOne();
                log.info("File : '$it'.");
                sourceFile
            }
            .windowed(batchSize, batchSize, true)
            .forEach {
                entityManager.flush()
                entityManager.clear()
            }
        observer.notifyDone()
    }
}
