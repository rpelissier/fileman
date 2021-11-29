package fr.dalae.fileman.web

import fr.dalae.fileman.repository.SourceDirRepository
import fr.dalae.fileman.service.SourceDirService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Path

@RestController
class ApplicationRestController {

    @Autowired
    private lateinit var sourceDirRepository: SourceDirRepository

    @Autowired
    private lateinit var sourceDirService: SourceDirService

    @GetMapping("/directories")
    fun getDirectories() : List<String>{
        return sourceDirRepository.findAll().map { it.path.toString() }
    }

    @PutMapping("/directory")
    fun putDirectory(@Param("path") path : String){
        sourceDirService.merge(Path.of(path))
    }
}