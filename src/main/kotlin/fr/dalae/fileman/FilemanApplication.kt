package fr.dalae.fileman

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class FilemanApplication

fun main(args: Array<String>) {
    runApplication<FilemanApplication>(*args)
}
