package fr.dalae.fileman

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.nio.file.Path

@SpringBootTest
@ActiveProfiles("test")
@Disabled
class BinaryScannerTest {

    @Autowired
    lateinit var directoryLoader: DirectoryLoader

    @Autowired
    lateinit var binaryScanner: BinaryScanner

    @Test
    fun test() {
        val sourceDirPath = Path.of("/Users/renaud/Downloads")
        directoryLoader.load(sourceDirPath)
        binaryScanner.scan(sourceDirPath)
    }
}
