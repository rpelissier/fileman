package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.Origin
import fr.dalae.fileman.domain.Source
import java.io.File
import java.util.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class RepositoriesTest {
    @Autowired
    lateinit var documentRepository: DocumentRepository

    @Autowired
    lateinit var originRepository: OriginRepository

    @Test
    fun testRepo() {

        val doc1 = Document(".txt", File("/ae439f10b"), Date(), 100)

        val origin = Origin(Source("/Volumes/HDD1"), "renaud/test.txt", doc1)

        originRepository.save(origin)
    }
}
