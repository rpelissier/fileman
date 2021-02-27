package fr.dalae.fileman.file

import java.nio.file.Path
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

class FileWalkerTest {
    private val log = LoggerFactory.getLogger(javaClass)
    private val rootPath = Path.of("build/test")

    @Before
    fun before() {
        val files = listOf(
            FileDescriptor("test.txt", "2015-02-20T06:30:00.000", 2.5.MB()),
            FileDescriptor("test2.txt", "2016-02-20T06:30:00.000", 7.KB())
        )
        FileTreeGenerator().generate(rootPath, files.stream())
    }

    @Test
    fun test() {
        log.info("Test")
    }
}
