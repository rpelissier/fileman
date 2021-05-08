package fr.dalae.fileman.service

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.domain.SourceFile
import fr.dalae.fileman.repository.SourceFileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class SourceFileService {

    @Autowired
    lateinit var sourceFileRepository: SourceFileRepository

    fun save(sourceDir: SourceDir, relativePath: Path, document: Document): SourceFile {
        var sourceFile = sourceFileRepository.findBySourceDirAndRelativePath(sourceDir, relativePath)
        if(sourceFile == null){
            sourceFile = SourceFile(sourceDir, relativePath, document)
        }else{
            sourceFile.documentHistory.add(sourceFile.document)
            sourceFile.document = document
        }
        sourceFileRepository.save(sourceFile)
        return sourceFile
    }
}