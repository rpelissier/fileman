package fr.dalae.fileman

import java.io.File
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DirectoryLoaderTest {

    @Autowired
    lateinit var directoryLoader: DirectoryLoader

    @Test
    fun test() {
        directoryLoader.load(File("."))
    }
}
