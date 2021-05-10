package fr.dalae.fileman.service

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.repository.SourceDirRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class SourceDirService {

    @Autowired
    private lateinit var sourceDirRepository: SourceDirRepository

    fun merge(sourceDirPath: Path): SourceDir {
        return sourceDirRepository
            .findByPath(sourceDirPath)
            ?: sourceDirRepository.save(SourceDir(sourceDirPath))

    }

}