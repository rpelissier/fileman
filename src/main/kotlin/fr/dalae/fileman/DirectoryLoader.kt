package fr.dalae.fileman

import fr.dalae.fileman.repository.OriginRepository
import java.io.File
import java.nio.file.Path
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DirectoryLoader {

    @Autowired
    lateinit var originRepository: OriginRepository

    private val originParser = OriginParser()

    var observer = CountingObserver.loggingObserver(10)

    fun load(directory: File) {
        val rootPath = Path.of(directory.path)
        directory.walkTopDown()
            .filter { it != directory } // Do not return the directory itself
            .forEach {
                val path = Path.of(it.path)
                val relativePath = rootPath.relativize(path)
                val origin = originParser.parse(directory, relativePath.toFile())
                originRepository.save(origin)
                observer.notifyOne()
            }
        observer.notifyDone()
    }
}
