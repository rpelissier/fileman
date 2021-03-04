package fr.dalae.fileman.file

import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import org.junit.Test
import org.slf4j.LoggerFactory

class WalkFileTreeTest {

    private val log = LoggerFactory.getLogger(javaClass)
    val followSymbolicLink = true

    val fileVisitor = object : SimpleFileVisitor<Path>() {
        override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
            log.info("Dir : '$dir'.")
            if (!followSymbolicLink && attrs.isSymbolicLink)
                return FileVisitResult.SKIP_SUBTREE
            return FileVisitResult.CONTINUE
        }

        override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
            log.info("File : '$file'.")
            return FileVisitResult.CONTINUE
        }

        override fun visitFileFailed(file: Path, e: IOException): FileVisitResult {
            log.error("Error while visiting file '$file'.", e)
            return FileVisitResult.CONTINUE
        }
    }

    @Test
    fun test() {
        val originDir = File("/Users/renaud/DEV/mts-filters")
        Files.walkFileTree(originDir.toPath(), fileVisitor)
    }
}
