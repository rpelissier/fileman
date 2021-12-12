package fr.dalae.fileman.service

import fr.dalae.fileman.file.BLOCK
import fr.dalae.fileman.file.FileNode
import fr.dalae.fileman.file.NodeGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Path

@SpringBootTest
class BinaryServiceTest {

    @Autowired
    lateinit var sourceDirService: SourceDirService

    @Autowired
    lateinit var sourceFileService: SourceFileService

    @Autowired
    lateinit var binaryService: BinaryService


    private val log = LoggerFactory.getLogger(javaClass)
    private val rootPath = Path.of("build/test")
    private val nodeGenerator = NodeGenerator()

    @Test
    fun `Files with same size and date but different binary should lead to separated binaries`() {
        val path = Path.of("file1.txt")
        val fileNode = FileNode(rootPath.resolve(path), "2015-02-20T06:30:00.000", 3.BLOCK())
        nodeGenerator.generateExact(fileNode)

        val path2 = Path.of("file2.txt")
        val fileNode2 = FileNode(rootPath.resolve(path2), "2015-02-20T06:30:00.000", 3.BLOCK())
        nodeGenerator.generateExact(fileNode2)

        val sourceDir = sourceDirService.merge(rootPath)

        val sourceFile = sourceFileService.merge(sourceDir, path)
        val binary = binaryService.merge(sourceFile)
        log.info(binary.toString())
        Assertions.assertEquals(0, binary.hashes.size)

        val sourceFile2 = sourceFileService.merge(sourceDir, path2)
        val binary2 = binaryService.merge(sourceFile2)
        log.info(binary2.toString())
        Assertions.assertEquals(1, binary2.hashes.size)

        Assertions.assertEquals(binary.lastModifiedEpochMs, binary2.lastModifiedEpochMs)
        Assertions.assertEquals(binary.size, binary2.size)
        Assertions.assertNotEquals(binary.id, binary2.id)
    }

    @Test
    fun `Files with same size and date and same binary should lead to same binaries`() {
        val path = Path.of("file3.txt")
        val fileNode = FileNode(rootPath.resolve(path), "2015-02-20T06:30:00.000", 5.BLOCK())
        val randomSeed = System.currentTimeMillis()
        nodeGenerator.generateExact(fileNode, randomSeed)

        val path2 = Path.of("file4.txt")
        val fileNode2 = FileNode(rootPath.resolve(path2), "2015-02-20T06:30:00.000", 5.BLOCK())
        nodeGenerator.generateExact(fileNode2, randomSeed)

        val sourceDir = sourceDirService.merge(rootPath)
        val sourceFile = sourceFileService.merge(sourceDir, path)
        var binary = binaryService.merge(sourceFile)
        log.info(binary.toString())

        val binary2 = binaryService.merge(sourceFile)
        log.info(binary2.toString())

        Assertions.assertEquals(binary.id, binary2.id)
    }
}