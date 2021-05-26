package fr.dalae.fileman.file

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.nio.file.Path

class FileUtilsTest {

    private val log = LoggerFactory.getLogger(javaClass)
    private val rootPath = Path.of("build/test/${FileUtilsTest::class.simpleName}")

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
    fun test(){
        val foundFiles = FileUtils.fileWalkingSequence(rootPath).toList()
        Assertions.assertEquals(2, foundFiles.size)
        Assertions.assertTrue(foundFiles.contains(file1.path))
        Assertions.assertTrue(foundFiles.contains(dir1File2.path))
        Assertions.assertFalse(foundFiles.contains(symFile1.path))
    }
}