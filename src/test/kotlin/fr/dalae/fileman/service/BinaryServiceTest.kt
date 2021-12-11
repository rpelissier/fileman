package fr.dalae.fileman.service

import fr.dalae.fileman.domain.BLOCK
import fr.dalae.fileman.domain.SourceFile
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
    lateinit var binaryService: BinaryService


    private val log = LoggerFactory.getLogger(javaClass)
    private val rootPath = Path.of("build/test")
    private val nodeGenerator = NodeGenerator()

    @Test
    fun `Files with same size and date but different binary should lead to separated documents`() {
        val path = Path.of("file1.txt")
        val fileNode = FileNode(rootPath.resolve(path), "2015-02-20T06:30:00.000", 3.BLOCK())
        nodeGenerator.generateExact(fileNode)

        val path2 = Path.of("file2.txt")
        val fileNode2 = FileNode(rootPath.resolve(path2), "2015-02-20T06:30:00.000", 3.BLOCK())
        nodeGenerator.generateExact(fileNode2)

        var sourceDir = sourceDirService.merge(rootPath)

        val document = binaryService.merge(SourceFile.create(sourceDir, path))
        log.info(document.toString())
        Assertions.assertEquals(0, document.hashes.size)

        val document2 = binaryService.merge(SourceFile.create(sourceDir, path2))
        log.info(document2.toString())
        Assertions.assertEquals(1, document2.hashes.size)

        Assertions.assertEquals(document.lastModifiedEpochMs, document2.lastModifiedEpochMs)
        Assertions.assertEquals(document.size, document2.size)
        Assertions.assertNotEquals(document.id, document2.id)
    }

    @Test
    fun `Files with same size and date and same binary should lead to same documents`() {
        val path = Path.of("file1.txt")
        val fileNode = FileNode(rootPath.resolve(path), "2015-02-20T06:30:00.000", 5.BLOCK())
        val randomSeed = System.currentTimeMillis()
        nodeGenerator.generateExact(fileNode, randomSeed)

        val path2 = Path.of("file2.txt")
        val fileNode2 = FileNode(rootPath.resolve(path2), "2015-02-20T06:30:00.000", 5.BLOCK())
        nodeGenerator.generateExact(fileNode2, randomSeed)

        var sourceDir = sourceDirService.merge(rootPath)

        var document = binaryService.merge(SourceFile.create(sourceDir, path))
        log.info(document.toString())

        val document2 = binaryService.merge(SourceFile.create(sourceDir, path2))
        document = binaryService.refresh(document)
        log.info(document2.toString())

        Assertions.assertEquals(document.id, document2.id)
    }
}