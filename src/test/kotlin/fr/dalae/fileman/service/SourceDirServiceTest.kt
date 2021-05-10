package fr.dalae.fileman.service

import fr.dalae.fileman.domain.SourceDir
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Path

@SpringBootTest
class SourceDirServiceTest {

    @Autowired
    lateinit var sourceDirService: SourceDirService

    @Test
    fun `Should merge duplicate sourceDir`(){
        val path = Path.of("sourceDir")
        val sourceDir1 = sourceDirService.merge(path)
        val sourceDir2 = sourceDirService.merge(path)

        Assertions.assertEquals(sourceDir1.id, sourceDir2.id)
    }

    @Test
    fun `Should not merge different sourceDir`(){
        val path1 = Path.of("sourceDir1")
        val path2= Path.of("sourceDir2")
        val sourceDir1 = sourceDirService.merge(path1)
        val sourceDir2 = sourceDirService.merge(path2)

        Assertions.assertNotEquals(sourceDir1.id, sourceDir2.id)
    }

}