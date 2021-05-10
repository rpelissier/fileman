package fr.dalae.fileman.service

import fr.dalae.fileman.ApplicationProperties
import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.file.HashSuite
import fr.dalae.fileman.repository.DocumentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@Service
class DocumentService(conf: ApplicationProperties) {

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

    fun refresh(document: Document):Document{
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
        val siblingDocs = docRepository.findByLastModifiedEpochMsAndSize(
            lastModified,
            size
        )

        siblingDocs
            .sortedByDescending { it.hashes.length }
            .forEach { siblingDoc ->
            //If same binary don't create a new doc use this one
            val siblingHash = hash(siblingDoc)
            val provedDifferent = HashSuite.hashUntilProvedDifferent(hashSuite, siblingHash)
            siblingDoc.hashes = siblingHash.blockHashes
            docRepository.save(siblingDoc) //Hash might have changed
            if (!provedDifferent) return siblingDoc
        }
        //Every siblings are proved different, create a new doc
        val doc = newDoc(lastModified, size, ext, hashSuite)
        return docRepository.save(doc)
    }

    private fun hash(document: Document): HashSuite {
        val path = storageDir.resolve(document.storageRelativePath)
        return HashSuite(path, document.hashes)
    }

    private fun newDoc(lastModified: Long, size: Long, ext: String, hashSuite: HashSuite): Document {
        val storageRelativePath = hardLinkToStorage(hashSuite.path)
        return Document(
            lastModified,
            size,
            hashSuite.blockHashes,
            storageRelativePath,
            ext
        )
    }

    private fun hardLinkToStorage(existingPath: Path): Path {
        val relativePath = Path.of(UUID.randomUUID().toString())
        val absPath = storageDir.resolve(relativePath)
        Files.createLink(absPath, existingPath);
        return relativePath
    }




}