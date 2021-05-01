package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.SourceDir
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.nio.file.Path

@DataJpaTest
class SourceDirRepositoryTest {
    @Autowired
    lateinit var originRepository: SourceDirRepository

    @Test
    fun test(){
        originRepository.save(SourceDir(Path.of("path/test")))
    }
}