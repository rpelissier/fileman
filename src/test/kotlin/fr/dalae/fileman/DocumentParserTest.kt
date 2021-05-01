package fr.dalae.fileman

import fr.dalae.fileman.domain.SourceDir
import org.junit.jupiter.api.Test
import java.nio.file.Path
import org.slf4j.LoggerFactory

class DocumentParserTest {

    val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun test() {
        val sourceDir = SourceDir(Path.of("/Users/renaud/Downloads"))
        val documentParser = DocumentParser(sourceDir)
        val sourceDirPath = sourceDir.path
        val sourceDirFile = sourceDirPath.toFile()
        sourceDirFile.walkTopDown().filter { it != sourceDirFile }.forEach {
            val path = Path.of(it.path)
            val relativePath = sourceDirPath.relativize(path)
            // log.info(relativePath.toString())
            log.info(documentParser.parse(relativePath).toString())
        }
    }
}
