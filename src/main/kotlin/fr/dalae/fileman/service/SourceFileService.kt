package fr.dalae.fileman.service

import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.domain.SourceFile
import fr.dalae.fileman.repository.SourceFileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class SourceFileService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var sourceFileRepository: SourceFileRepository

    @Autowired
    lateinit var documentService: DocumentService

    fun merge(sourceDir: SourceDir, relativePath: Path): SourceFile {

        log.debug("Merging path '$relativePath'" )
        var attachedSourceFile = sourceFileRepository.findBySourceDirAndRelativePath(sourceDir, relativePath)

        if (attachedSourceFile == null) {
            //First time we see this file
            val attachedDocument = documentService.merge(sourceDir, relativePath)
            log.debug("Merged document '${attachedDocument.id}'" )
            val sourceFile = SourceFile(sourceDir, relativePath, attachedDocument)
            attachedSourceFile = sourceFileRepository.save(sourceFile)!!
            log.debug("Adding sourceFile ${attachedSourceFile.id} to doc '${attachedDocument.id}'" )
            attachedDocument.sourceFiles.add(attachedSourceFile)
        } else {
            //This file is already present.
            val file = sourceDir.path.resolve(relativePath).toFile()
            val lastModified = file.lastModified()
            val size = file.length()

            //Don't check the doc hashes if same lastmodified and same size, otherwise it means full hash
            val existingDoc = attachedSourceFile.document
            log.debug("Found existing sourceFile '${attachedSourceFile.id}'" )
            if (existingDoc.size == size && existingDoc.lastModifiedEpochMs == lastModified)
                return attachedSourceFile

            //The doc might have changed and we need to hash to prove difference
            val attachedDocument = documentService.merge(sourceDir, relativePath)
            if (attachedDocument != attachedSourceFile.document) {
                attachedSourceFile.documentHistory.add(attachedSourceFile.document)
                attachedSourceFile.document = attachedDocument
            }
        }
        sourceFileRepository.save(attachedSourceFile)
        return attachedSourceFile
    }
}