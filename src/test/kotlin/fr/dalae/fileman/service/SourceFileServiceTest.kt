package fr.dalae.fileman.service

import fr.dalae.fileman.domain.BLOCK
import fr.dalae.fileman.domain.SourceFile
import fr.dalae.fileman.file.FileNode
import fr.dalae.fileman.file.NodeGenerator
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Path

@SpringBootTest
class SourceFileServiceTest {

    private val log = LoggerFactory.getLogger(javaClass)
    private val rootPath = Path.of("build/test")
    private val nodeGenerator = NodeGenerator()

    @Autowired
    lateinit var sourceDirService: SourceDirService

    @Autowired
    lateinit var binaryService: BinaryService

    @Autowired
    lateinit var sourceFileService: SourceFileService


    @Test
    fun `should do`(){
        val path = Path.of("file1.txt")
        val fileNode = FileNode(rootPath.resolve(path), "2015-02-20T06:30:00.000", 3.BLOCK())
        nodeGenerator.generateExact(fileNode)

        val path2 = Path.of("file2.txt")
        val fileNode2 = FileNode(rootPath.resolve(path2), "2015-02-20T06:30:00.000", 3.BLOCK())
        nodeGenerator.generateExact(fileNode2)

        var sourceDir = sourceDirService.merge(rootPath)

        val document = binaryService.merge(SourceFile.create(sourceDir, path))
        log.info(document.toString())

        val document2 = binaryService.merge(SourceFile.create(sourceDir, path2))
        log.info(document2.toString())

        sourceFileService.merge(SourceFile.create(sourceDir, path))
        sourceFileService.merge(SourceFile.create(sourceDir, path2))
    }
}