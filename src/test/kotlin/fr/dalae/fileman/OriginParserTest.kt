package fr.dalae.fileman

import java.io.File
import java.nio.file.Path
import org.junit.Test
import org.slf4j.LoggerFactory

class OriginParserTest {

    val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun test() {
        val originDir = File("/Users/renaud/Downloads")
        val originParser = OriginParser(originDir)
        val rootPath = Path.of(originDir.path)
        originDir.walkTopDown().filter { it != originDir }.forEach {
            val path = Path.of(it.path)
            val relativePath = rootPath.relativize(path)
            // log.info(relativePath.toString())
            log.info(originParser.parse(relativePath.toFile()).toString())
        }
    }
}
