package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.Origin
import java.io.File
import java.util.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class RepositoriesTest {
    @Autowired
    lateinit var originRepository: OriginRepository

    @Test
    fun testRepo() {
        val doc1 = Document("toto", 0L, 500, File("ae439f10b"), "txt", setOf("voiture", "rouge"))
        val origin = Origin(File("/Volumes/HDD1"), File("toto.txt"), doc1)
        originRepository.save(origin)
    }
}
