package fr.dalae.fileman.service

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.file.*
import fr.dalae.fileman.repository.SourceDirRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Path

@SpringBootTest
class DocumentServiceTest {

    @Autowired
    lateinit var sourceDirRepository: SourceDirRepository

    @Autowired
    lateinit var documentService: DocumentService


    private val log = LoggerFactory.getLogger(javaClass)
    private val rootPath = Path.of("build/test")

    @BeforeEach
    fun before() {
        rootPath.toFile().deleteRecursively()
    }


    @Test
    fun test() {
        val nodeGenerator = NodeGenerator()

        val fileRelativePath = Path.of("file1.txt")
        val fileNode = FileNode(fileRelativePath, "2015-02-20T06:30:00.000", 2.5.MB())

        var sourceDir = SourceDir(rootPath)
        sourceDir = sourceDirRepository.save(sourceDir)

        nodeGenerator.generate(rootPath, fileNode)
        val document = documentService.save(sourceDir, fileRelativePath)
        log.info(document.toString())

        nodeGenerator.generate(rootPath, fileNode)
        nodeGenerator.randomChange(rootPath, fileNode, 0,32)

        val document2 = documentService.save(sourceDir, fileRelativePath)
        log.info(document2.toString())
    }
}