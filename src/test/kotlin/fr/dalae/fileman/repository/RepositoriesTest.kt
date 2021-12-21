package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.entity.Binary
import fr.dalae.fileman.domain.entity.SourceDir
import fr.dalae.fileman.domain.entity.SourceFile
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
    lateinit var binaryRepository: BinaryRepository

    @Test
    fun `Should persist a SourceFile entity`() {

        val sourceDir = SourceDir(Path.of("/Volumes/HDD1"))
        sourceDirRepository.save(sourceDir)

        val bin1 = Binary(0L, 500, arrayListOf())
        binaryRepository.save(bin1)

        val origin = SourceFile.create(sourceDir, Path.of("toto.txt"))
        sourceFileRepository.save(origin)
    }

    @Test
    fun `Should persist a SourceDir entity`() {
        val sourceDir = SourceDir(Path.of("/Volumes/HDD1"))
        sourceDirRepository.save(sourceDir)
    }
}
