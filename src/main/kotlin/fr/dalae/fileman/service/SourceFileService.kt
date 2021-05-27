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

    @Autowired
    lateinit var documentService: DocumentService

    fun merge(sourceDir: SourceDir, relativePath: Path): SourceFile {

        var sourceFile = sourceFileRepository.findBySourceDirAndRelativePath(sourceDir, relativePath)

        if (sourceFile == null) {
            //First time we see this file
            val document = documentService.merge(sourceDir, relativePath)
            sourceFile = SourceFile(sourceDir, relativePath, document)
            document.sourcefiles.add(sourceFile)
        } else {
            //This file is already present.
            val file = sourceDir.path.resolve(relativePath).toFile()
            val lastModified = file.lastModified()
            val size = file.length()

            //Don't check the doc hashes if same lastmodified and same size, otherwise it means full hash
            val existingDoc = sourceFile.document
            if (existingDoc.size == size && existingDoc.lastModifiedEpochMs == lastModified)
                return sourceFile

            //The doc might have changed and we need to hash to prove difference
            val document = documentService.merge(sourceDir, relativePath)
            if (document != sourceFile.document) {
                sourceFile.documentHistory.add(sourceFile.document)
                sourceFile.document = document
            }
        }
        sourceFileRepository.save(sourceFile)
        return sourceFile
    }
}