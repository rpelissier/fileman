package fr.dalae.fileman

import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@RunWith(MockitoJUnitRunner::class)
@DataJpaTest
class FilemanApplicationTests {

    @Test
    fun contextLoads() {
    }
}
