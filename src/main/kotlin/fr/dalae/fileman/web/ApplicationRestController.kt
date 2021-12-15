package fr.dalae.fileman.web

import fr.dalae.fileman.DirectoryLoader
import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.service.SourceDirService
import fr.dalae.fileman.service.SourceFileService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Path

@RestController
class ApplicationRestController {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var sourceDirService: SourceDirService

    @Autowired
    private lateinit var sourceFileService: SourceFileService

    @Autowired
    private lateinit var directoryLoader: DirectoryLoader

    @GetMapping("/directories")
    fun getDirectories(): List<SourceDir> {
        return sourceDirService.list()
    }

    @PutMapping("/directory")
    fun putDirectory(@Param("path") path: String): String {
        log.info("PUT path '$path'")
        val sourceDir = directoryLoader.load(Path.of(path))
        return sourceFileService.countAll(sourceDir).toString()
    }
}