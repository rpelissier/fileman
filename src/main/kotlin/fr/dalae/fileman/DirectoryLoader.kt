package fr.dalae.fileman

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.domain.SourceFile
import fr.dalae.fileman.repository.DocumentRepository
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
    lateinit var documentRepository: DocumentRepository

    val storageDir = Path.of(config.storagePath)
        .apply { toFile().mkdirs() }

    val batchSize = config.batchSize
    var observer = CountingObserver.loggingObserver(batchSize)

    @Transactional
    fun load(sourceDir: SourceDir) {
        entityManager.persist(sourceDir)

        // NOTE alternative is to use NIO Files DirectoryStream<Path> to avoid following symlink for
        // both files and directories
        // https://www.jmdoudoux.fr/java/dej/chap-nio2.htm
        val originDir = sourceDir.path.toFile()
        val documentParser = DocumentParser(sourceDir)
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
                var doc = documentParser.parse(relativePath)
                doc = entityManager.merge(doc)
                val sourceFile = SourceFile(sourceDir,relativePath, doc)
                entityManager.merge(sourceFile)
                observer.notifyOne();
                log.info("File : '$relativePath'.");
            }
            .windowed(batchSize, batchSize, true)
            .forEach {
                entityManager.flush()
                entityManager.clear()
            }
        observer.notifyDone()
    }


    private fun creatDocIfNew(sourceFile: SourceFile) {
        val doc = sourceFile.document
        if (!documentRepository.existsById(doc.id)) {
            documentRepository.save(doc)
            val docAbsPath = doc.resolveInto(storageDir)
            Files.createDirectories(docAbsPath.parent)
            Files.createLink(docAbsPath, sourceFile.fullPath)
        }
    }
}
