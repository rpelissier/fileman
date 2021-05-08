package fr.dalae.fileman.service

import fr.dalae.fileman.ApplicationProperties
import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.file.Hash
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

    fun save(sourceDir: SourceDir, relativePath: Path): Document {
        val path = sourceDir.path.resolve(relativePath)
        val hash = Hash(path)
        return merge(hash)
    }


    /**
     * Compare docCandidate hash with every document that have the same date and size.
     * Hash only the beginning of the file that are enough to demonstrate the difference.
     * The only case where we need to hash the entire file is to demonstrate equality.
     */
    private fun merge(hash: Hash): Document {
        val file = hash.path.toFile()
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
            val provedDifferent = hashUntilProvedDifferent(hash, siblingHash)
            siblingDoc.hashes = siblingHash.blockHashes
            docRepository.save(siblingDoc) //Hash might have changed
            if (!provedDifferent) return siblingDoc
        }
        //Every siblings are proved different, create a new doc
        val doc = newDoc(lastModified, size, ext, hash)
        return docRepository.save(doc)
    }

    private fun hash(document: Document): Hash {
        val path = storageDir.resolve(document.storageRelativePath)
        return Hash(path, document.hashes)
    }

    private fun newDoc(lastModified: Long, size: Long, ext: String, hash: Hash): Document {
        val storageRelativePath = hardLinkToStorage(hash.path)
        return Document(
            lastModified,
            size,
            hash.blockHashes,
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

    /**
     * Compute the necessary hash to demonstrate difference or default to equality
     */
    private fun hashUntilProvedDifferent(pHash1: Hash, pHash2: Hash): Boolean {
        if (areProvedDifferent(pHash1, pHash2)) return true
        // More hash needed
        val nextHash = if (pHash1.blockHashedCount < pHash2.blockHashedCount) pHash1.hashNext() else pHash2.hashNext()
        //No more hash available means equality
        if (nextHash == "") return false
        //New iteration as a new hash is available
        return hashUntilProvedDifferent(pHash1, pHash2)
    }

    /**
     * Document are for sure differents if current hashes don't start
     * with the same sequence
     */
    private fun areProvedDifferent(pHash1: Hash, pHash2: Hash): Boolean {
        return !pHash1.blockHashes.startsWith(pHash2.blockHashes) && !pHash2.blockHashes.startsWith(pHash1.blockHashes)
    }


}
