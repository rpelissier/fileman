package fr.dalae.fileman

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FilemanApplication

fun main(args: Array<String>) {
    runApplication<FilemanApplication>(*args)
}
