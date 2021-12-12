package fr.dalae.fileman.service

import fr.dalae.fileman.ApplicationProperties
import fr.dalae.fileman.domain.Binary
import fr.dalae.fileman.domain.SourceFile
import fr.dalae.fileman.file.HashUtils
import fr.dalae.fileman.repository.BinaryRepository
import fr.dalae.fileman.repository.SourceFileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.nio.file.Path

@Service
class BinaryService(conf: ApplicationProperties) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var binaryRepository: BinaryRepository

    @Autowired
    lateinit var sourceFileRepository: SourceFileRepository


    private val storageDir = Path.of(conf.storageDir)

    init {
        storageDir.toFile().mkdirs()
    }

    @Transactional
    fun merge(attachedSourceFile: SourceFile): Binary {
        val binary = merge(attachedSourceFile.file)
        binary.sourceFiles.add(attachedSourceFile)
        binaryRepository.save(binary)
        return binary
    }

    fun refresh(document: Binary): Binary {
        return binaryRepository.findById(document.id).orElseThrow()
    }


    /**
     * Compare docCandidate hash with every document that have the same date and size.
     * Hash only the beginning of the file that are enough to demonstrate the difference.
     * The only case where we need to hash the entire file is to demonstrate equality.
     */
    private fun merge(file: File): Binary {
        val lastModified = file.lastModified()
        val size = file.length()
        val binary = Binary(lastModified, size)

        val attachedSiblingBinaries = binaryRepository.findByLastModifiedEpochMsAndSize(
            lastModified,
            size
        )

        log.debug("Found ${attachedSiblingBinaries.size} siblingDocs : ${attachedSiblingBinaries.map { it.id }}")

        attachedSiblingBinaries
            .sortedByDescending { it.hashes.size }
            .forEach { attachedSiblingBin ->
                //If same binary don't create a new doc use this one
                val siblingFile = attachedSiblingBin.sourceFiles.first().file
                val provedDifferent =
                    HashUtils.hashUntilProvedDifferent(file, binary.hashes, siblingFile, attachedSiblingBin.hashes)
                binaryRepository.save(attachedSiblingBin) //Hash might have changed
                if (!provedDifferent) return attachedSiblingBin
            }
        //Every siblings are proved different, create a new doc
        log.debug("Creating new Binary for file $file with hashes ${binary.hashes}")
        return binaryRepository.save(binary)
    }


}
