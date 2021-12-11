package fr.dalae.fileman

import fr.dalae.fileman.service.BinaryService
import fr.dalae.fileman.service.SourceDirService
import fr.dalae.fileman.service.SourceFileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class DirectoryGenerator {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var sourceDirService: SourceDirService

    @Autowired
    lateinit var binaryService: BinaryService

    @Autowired
    lateinit var sourceFileService: SourceFileService


    fun generateFlatImageDir(outputDir: Path){

    }
}