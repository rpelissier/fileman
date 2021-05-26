package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.domain.SourceFile
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.nio.file.Path

@DataJpaTest
class RepositoriesTest {
    @Autowired
    lateinit var sourceDirRepository: SourceDirRepository

    @Autowired
    lateinit var sourceFileRepository: SourceFileRepository

    @Autowired
    lateinit var documentRepository: DocumentRepository

    @Test
    fun `Should persist a SourceFile entity`() {

        val sourceDir = SourceDir(Path.of("/Volumes/HDD1"))
        sourceDirRepository.save(sourceDir)

        val doc1 = Document(0L, 500, arrayListOf(), Path.of("some-storage-path/a2eff91a"), "txt")
        documentRepository.save(doc1)

        val origin = SourceFile(sourceDir, Path.of("toto.txt"), doc1)
        sourceFileRepository.save(origin)
    }

    @Test
    fun `Should persist a SourceDir entity`() {
        val sourceDir = SourceDir(Path.of("/Volumes/HDD1"))
        sourceDirRepository.save(sourceDir)
    }
}
