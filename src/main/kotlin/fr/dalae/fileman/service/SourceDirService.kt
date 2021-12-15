package fr.dalae.fileman.service

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.repository.SourceDirRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Service
class SourceDirService {

    @Autowired
    private lateinit var sourceDirRepository: SourceDirRepository

    @Transactional
    fun merge(sourceDirPath: Path): SourceDir {
        return sourceDirRepository
            .findByPath(sourceDirPath)
            ?: sourceDirRepository.save(SourceDir(sourceDirPath))
    }

    @Transactional
    fun list(): List<SourceDir> {
        return sourceDirRepository.findAll().filterNotNull()
    }

    @Transactional
    fun find(sourceDirPath: Path): SourceDir {
        return sourceDirRepository
            .findByPath(sourceDirPath)?:throw Exception("SourceDir $sourceDirPath not found.")
    }
}