package fr.dalae.fileman.file

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class NodeGeneratorTest {
    private val log = LoggerFactory.getLogger(javaClass)
    private val rootPath = Path.of("build/test/${NodeGenerator::class.simpleName}")

    private val file1 = FileNode("file1.txt", "2015-02-20T06:30:00.000", 2.5.MB())
    private val dir1File2 =FileNode("dir1/file2.txt", "2016-02-20T06:30:00.001", 7.KB())
    private val symDir2 =SymLinkNode("symDir2", "dir1")
    private val symFile1 =SymLinkNode("symFile1.txt", "file1.txt")

    @BeforeEach
    fun before() {
        rootPath.toFile().deleteRecursively()
        val files = listOf(
            file1,
            dir1File2,
            symDir2,
            symFile1
        )
        NodeGenerator().generateAll(rootPath, files.stream())
    }

    @Test
    fun `File tree can be found on the disk`() {
        log.info("Test")

        val foundFile1 = rootPath.resolve(file1.path).toFile()
        val foundDir1File2 = rootPath.resolve(dir1File2.path).toFile()
        val foundSymDir2 = rootPath.resolve(symDir2.path)
        val foundSymFile1 = rootPath.resolve(symFile1.path)

        Assertions.assertTrue(foundFile1.isFile)
        Assertions.assertEquals(FileUtils.toEpochMillis(file1.date),  foundFile1.lastModified())
        Assertions.assertEquals(file1.size, foundFile1.length())

        Assertions.assertTrue(foundDir1File2.isFile)
        Assertions.assertEquals(FileUtils.toEpochMillis(dir1File2.date),  foundDir1File2.lastModified())
        Assertions.assertEquals(dir1File2.size, foundDir1File2.length())

        Assertions.assertTrue(Files.isSymbolicLink(foundSymDir2))
        Assertions.assertEquals(symDir2.symbolicLinkTarget, rootPath.toAbsolutePath().relativize(Files.readSymbolicLink(foundSymDir2)))

        Assertions.assertTrue(Files.isSymbolicLink(foundSymFile1))
        Assertions.assertEquals(symFile1.symbolicLinkTarget, rootPath.toAbsolutePath().relativize(Files.readSymbolicLink(foundSymFile1)))
    }
}