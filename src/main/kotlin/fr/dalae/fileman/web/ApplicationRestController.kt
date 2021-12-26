package fr.dalae.fileman.web

import fr.dalae.fileman.DirectoryLoader
import fr.dalae.fileman.domain.SourceFilesSummary
import fr.dalae.fileman.domain.entity.SourceDir
import fr.dalae.fileman.service.SourceDirService
import fr.dalae.fileman.service.SourceFileService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Path

@RestController
@CrossOrigin(origins = ["http://localhost:3000"], maxAge = 3600)
class ApplicationRestController {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var sourceDirService: SourceDirService

    @Autowired
    private lateinit var sourceFileService: SourceFileService

    @Autowired
    private lateinit var directoryLoader: DirectoryLoader

    @GetMapping("/directories")
    fun getDirectories(): List<String> {
        log.info("GET directories")
        return sourceDirService.list().map { it.path.toString() }
    }

    @PutMapping("/directory")
    fun putDirectory(@Param("path") path: String): String {
        log.info("PUT directory '$path'")
        val sourceDir = directoryLoader.load(Path.of(path))
        return sourceFileService.countAll(sourceDir).toString()
    }

    @GetMapping("/directory/files")
    fun getDirectoryFiles(@Param("path") path: String): List<String> {
        log.info("GET directory files '$path'")
        return sourceFileService.findAll(path).map { it.relativePath.toString() }
    }

    @GetMapping("/directory/summary")
    fun getDirectorySummary(@Param("path") path: String): SourceFilesSummary {
        log.info("GET directory files '$path'")
        return sourceFileService.summarize(path)
    }
}