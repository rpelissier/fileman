package fr.dalae.fileman

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.service.DocumentService
import fr.dalae.fileman.service.SourceDirService
import fr.dalae.fileman.service.SourceFileService
import java.nio.file.Files
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

        // NOTE alternative is to use NIO Files DirectoryStream<Path> to avoid following symlink for
        // both files and directories
        // https://www.jmdoudoux.fr/java/dej/chap-nio2.htm
        val originDir = sourceDir.path.toFile()
        originDir
            .walkTopDown()
            .onEnter {
                log.info("Entering '${it.path}'.")
                !Files.isSymbolicLink(it.toPath())
            }
            .filter { it.isFile}
            .map { it.toPath() }
            .filter { !Files.isSymbolicLink(it) }
            .map {
                val relativePath = sourceDir.path.relativize(it)
                val document = documentService.merge(sourceDir, relativePath)
                val sourceFile = sourceFileService.merge(sourceDir, relativePath, document)
                observer.notifyOne();
                log.info("File : '$relativePath'.");
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
