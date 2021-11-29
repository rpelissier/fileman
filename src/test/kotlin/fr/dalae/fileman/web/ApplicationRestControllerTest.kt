package fr.dalae.fileman.web

import org.hamcrest.Matchers.equalTo
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders


@SpringBootTest
@AutoConfigureMockMvc
class ApplicationRestControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc


    @Test
    fun `Should create directory`(){
        mvc.perform(
            MockMvcRequestBuilders.get("/directories")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().string(equalTo("[]"))
            )

        val newDir = "newDir"
        mvc.perform(
            MockMvcRequestBuilders.put("/directory").param("path", newDir)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

        mvc.perform(
            MockMvcRequestBuilders.get("/directories")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().string(equalTo("[\"$newDir\"]"))
            )
    }
}