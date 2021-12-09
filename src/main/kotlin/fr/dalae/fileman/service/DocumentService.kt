package fr.dalae.fileman.service

import fr.dalae.fileman.ApplicationProperties
import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.file.HashSuite
import fr.dalae.fileman.repository.DocumentRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@Service
class DocumentService(conf: ApplicationProperties) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var docRepository: DocumentRepository

    private val storageDir = Path.of(conf.storageDir)

    init {
        storageDir.toFile().mkdirs()
    }

    fun merge(sourceDir: SourceDir, relativePath: Path): Document {
        val path = sourceDir.path.resolve(relativePath)
        val hash = HashSuite(path)
        return merge(hash)
    }

    fun refresh(document: Document): Document {
        return docRepository.findById(document.id).orElseThrow()
    }


    /**
     * Compare docCandidate hash with every document that have the same date and size.
     * Hash only the beginning of the file that are enough to demonstrate the difference.
     * The only case where we need to hash the entire file is to demonstrate equality.
     */
    private fun merge(hashSuite: HashSuite): Document {
        val file = hashSuite.path.toFile()
        val lastModified = file.lastModified()
        val size = file.length()
        val ext = file.extension
        val attachedSiblingDocs = docRepository.findByLastModifiedEpochMsAndSize(
            lastModified,
            size
        )

        log.debug("Found ${attachedSiblingDocs.size} siblingDocs : ${attachedSiblingDocs.map { it.id }}")

        attachedSiblingDocs
            .sortedByDescending { it.hashes.size }
            .forEach { attachedSiblingDoc ->
                //If same binary don't create a new doc use this one
                val siblingHash = hash(attachedSiblingDoc)
                val provedDifferent = HashSuite.hashUntilProvedDifferent(hashSuite, siblingHash)
                attachedSiblingDoc.hashes = siblingHash.blockHashes
                docRepository.save(attachedSiblingDoc) //Hash might have changed
                if (!provedDifferent) return attachedSiblingDoc
            }
        //Every siblings are proved different, create a new doc
        log.debug("Creating new doc for file $file with hashes ${hashSuite.blockHashes}")
        val doc = newDoc(lastModified, size, ext, hashSuite)
        return docRepository.save(doc)
    }

    private fun hash(document: Document): HashSuite {
        val path = document.sourceFiles.first().fullPath
        return HashSuite(path, document.hashes)
    }

    private fun newDoc(lastModified: Long, size: Long, ext: String, hashSuite: HashSuite): Document {
        return Document(
            lastModified,
            size,
            hashSuite.blockHashes,
            ext
        )
    }

    fun hardLinkToStorage(existingPath: Path): Path {
        val uuid = UUID.randomUUID().toString()
        //To avoid storing 100,000 in a flat directory structure
        //we use the first two pair of hexa to create 2 levels.
        val relativePath = Path.of(
            uuid.substring(0..1),
            uuid.substring(2..3),
            uuid.substring(4)
        )
        val absPath = storageDir.resolve(relativePath)
        absPath.parent.toFile().mkdirs()
        Files.createLink(absPath, existingPath);
        return relativePath
    }
}
