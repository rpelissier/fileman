package fr.dalae.fileman

import fr.dalae.fileman.domain.Origin
import fr.dalae.fileman.repository.DocumentRepository
import java.io.File
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
class DirectoryLoader(applicationProperties: ApplicationProperties) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var documentRepository: DocumentRepository

    private val storageDir = Path.of(applicationProperties.storagePath)
        .apply { toFile().mkdirs() }

    val batchSize = applicationProperties.batchSize
    var observer = CountingObserver.loggingObserver(batchSize)

    @Transactional
    fun load(originDir: File) {
        // TODO use Files.walkFileTree() to avoid following symlink for
        // both files and directories
        val originParser = OriginParser(originDir)
        originDir
            .walkTopDown()
            .filter { it.isFile } // Do not return the directory itself
            .map { it.relativeTo(originDir) }
            .map { originParser.parse(it) }
            .map { observer.notifyOne(); it }
            .windowed(batchSize, batchSize, true)
            .forEach { mergeAllAndFlush(it) }
        observer.notifyDone()
    }

    private fun mergeAllAndFlush(buffer: List<Origin>) {
        buffer.forEach {
            creatDocIfNew(it)
            entityManager.merge(it)
        }
        entityManager.flush()
        entityManager.clear()
    }

    private fun creatDocIfNew(origin: Origin) {
        val doc = origin.document
        if (!documentRepository.existsById(doc.id)) {
            documentRepository.save(doc)
            val docAbsPath = doc.resolveInto(storageDir)
            Files.createDirectories(docAbsPath.parent)
            Files.createLink(docAbsPath, origin.absPath)
        }
    }
}
