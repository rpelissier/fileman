package fr.dalae.fileman

import fr.dalae.fileman.file.FileUtils
import fr.dalae.fileman.service.BinaryService
import fr.dalae.fileman.service.SourceDirService
import fr.dalae.fileman.service.SourceFileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Path
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Service
class BinaryScanner(config: ApplicationProperties) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var sourceDirService: SourceDirService

    @Autowired
    lateinit var sourceFileService: SourceFileService

    @Autowired
    lateinit var binaryService: BinaryService

    final val batchSize = config.batchSize
    var observer = CountingObserver.loggingObserver(batchSize)

    @Transactional
    fun scan(sourceDirPath: Path) {
        val sourceDir = sourceDirService.find(sourceDirPath)
        val sourceFiles = sourceFileService.findAll(sourceDir)
        sourceFiles
            .map {
                val sourceFile = binaryService.merge(it)
                observer.notifyOne();
                log.info("File : '$it'.");
                sourceFile
            }
            .windowed(batchSize, batchSize, true)
            .forEach { _ ->
                entityManager.flush()
                entityManager.clear()
            }
        observer.notifyDone()
    }
}
