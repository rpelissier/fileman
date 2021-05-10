package fr.dalae.fileman

import fr.dalae.fileman.domain.SourceDir
import java.io.File
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.nio.file.Path

@SpringBootTest
@ActiveProfiles("test")
class DirectoryLoaderTest {

    @Autowired
    lateinit var directoryLoader: DirectoryLoader

    @Test
    fun test() {
        directoryLoader.storageDir.toFile().apply {
            deleteRecursively()
            mkdirs()
        }
        val sourceDirPath = Path.of("/Users/renaud/DEV")
        directoryLoader.load(sourceDirPath)
    }
}
