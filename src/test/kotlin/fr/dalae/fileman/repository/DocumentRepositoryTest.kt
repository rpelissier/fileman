package fr.dalae.fileman.repository

import Document
import java.io.File
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DocumentRepositoryTest {
    @Autowired
    lateinit var docRepository: DocRepository

    @Test
    fun testRepo() {

        val doc1 = Document(".txt", File("/renaud/test.txt"))

        docRepository.save(doc1)
    }
}
