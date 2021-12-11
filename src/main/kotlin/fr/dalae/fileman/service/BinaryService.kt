package fr.dalae.fileman.service

import fr.dalae.fileman.ApplicationProperties
import fr.dalae.fileman.domain.Binary
import fr.dalae.fileman.domain.SourceFile
import fr.dalae.fileman.file.FileUtils
import fr.dalae.fileman.repository.BinaryRepository
import fr.dalae.fileman.repository.SourceFileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path
import kotlin.math.min

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

    fun merge(sourceFile: SourceFile): Binary {
        val attachedSourceFile = sourceFileRepository.save(sourceFile)
        val binary = merge(sourceFile.file)
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
                val provedDifferent = hashUntilProvedDifferent(file, binary, siblingFile, attachedSiblingBin)
                binaryRepository.save(attachedSiblingBin) //Hash might have changed
                if (!provedDifferent) return attachedSiblingBin
            }
        //Every siblings are proved different, create a new doc
        log.debug("Creating new doc for file $file with hashes ${binary.hashes}")
        return binaryRepository.save(binary)
    }


    /**
     * Compute the necessary hash to demonstrate difference or default to equality
     */
    fun hashUntilProvedDifferent(file1: File, bin1: Binary, file2: File, bin2: Binary): Boolean {
        if (isProvedDifferent(bin1, bin2)) return true

        //If we are here every existing hash are equals
        while (true) {
            // More hash needed
            val thisHash: String
            val otherHash: String
            if (bin1.hashes.size < bin2.hashes.size) {
                thisHash = hashNext(file1, bin1)
                otherHash = bin2.hashes[bin1.hashes.lastIndex]
                //If the smallest file is over, they are different.
                if (thisHash.isEmpty()) return true
            } else if (bin1.hashes.size > bin2.hashes.size) {
                otherHash = hashNext(file2, bin2)
                thisHash = bin1.hashes[bin2.hashes.lastIndex]
                //If the smallest file is over, they are different.
                if (otherHash.isEmpty()) return true
            } else {
                //File have the same number of block. We need two extra blocks to progress
                thisHash = hashNext(file1, bin1)
                otherHash = hashNext(file2, bin2)
                //If both files are over, they are equals.
                if (thisHash.isEmpty() && otherHash.isEmpty()) return false
            }

            //If hashes are different, they are different.
            if (thisHash != otherHash) return true

            //If we are here we need another run
        }
    }


    private fun isProvedDifferent(bin1: Binary, bin2: Binary): Boolean {
        for (i in 0 until min(bin1.hashes.size, bin2.hashes.size)) {
            if (bin1.hashes[i] != bin2.hashes[i])
                return true
        }
        return false
    }


    /**
     * Hash one more block and update the hashes list.
     * Return empty string if no more block can be hashed
     */
    private fun hashNext(file: File, binary: Binary): String {
        val hash = FileUtils.hashBlock(file, binary.hashes.size, Binary.HASH_BLOCK_SIZE)
        if (hash.isNotEmpty()) binary.hashes.add(hash)
        return hash
    }
}
