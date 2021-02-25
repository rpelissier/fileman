package fr.dalae.fileman

import fr.dalae.fileman.domain.Origin
import java.io.File
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DirectoryLoader(val batchSize: Int = 50) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @PersistenceContext
    lateinit var entityManager: EntityManager

    private val originParser = OriginParser()

    var observer = CountingObserver.loggingObserver(10)

    @Transactional
    fun load(directory: File) {
        directory
            .walkTopDown()
            .filter { it != directory } // Do not return the directory itself
            .map { it.relativeTo(directory) }
            .map { originParser.parse(directory, it) }
            .map { observer.notifyOne(); it }
            .windowed(batchSize, batchSize, true)
            .forEach { mergeAll(it) }
        observer.notifyDone()
    }

    private fun mergeAll(buffer: List<Origin>) {
        buffer.forEach {
            entityManager.merge(it)
        }
        entityManager.flush()
        entityManager.clear()
    }
}
