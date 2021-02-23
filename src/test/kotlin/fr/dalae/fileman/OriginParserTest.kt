package fr.dalae.fileman

import java.io.File
import java.nio.file.Path
import org.junit.Test
import org.slf4j.LoggerFactory

class OriginParserTest {

    val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun test() {
        val originParser = OriginParser()
        val rootDirectory = File("/Users/renaud/Downloads")
        val rootPath = Path.of(rootDirectory.path)
        rootDirectory.walkTopDown().filter { it != rootDirectory }.forEach {
            val path = Path.of(it.path)
            val relativePath = rootPath.relativize(path)
            // log.info(relativePath.toString())
            log.info(originParser.parse(rootDirectory, relativePath.toFile()).toString())
        }
    }
}
