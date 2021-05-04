package fr.dalae.fileman.domain

import fr.dalae.fileman.file.FileUtils
import org.springframework.util.DigestUtils
import java.nio.file.Path
import javax.persistence.*

@Entity
@Table(
    indexes = [
        Index(columnList = "lastModifiedEpochMs, size, hashes", unique = true)
    ]
)
@SequenceGenerator(initialValue = 1, name = "generator", sequenceName = "documentSeq")
data class Document(

    val lastModifiedEpochMs: Long,

    val size: Long,

    @Lob
    @Basic(fetch = FetchType.LAZY)
    var hashes: String = "",

    @Convert(converter = PathConverter::class)
    @Column(columnDefinition = "varchar(512)")
    val relativePath: Path,

    val extension: String,

    ) : DomainEntity() {

    companion object {
        val HASH_BLOCK_SIZE = 1024 * 10
        val HASH_N_CHAR = 32 //32 hexadecimal is 16 Bytes / 128 bits
    }

    fun resolveInto(storageDir: Path): Path {
        return storageDir.resolve(relativePath)
    }

    val blockHashedCount: Int
        get() = hashes.length / HASH_N_CHAR

    val name: String
        get() = relativePath.fileName.toString()

    /**
     * Hash one more block and update the hashes list.
     * Return empty string if no more block can be hashed
     */
    fun hashNext(): String {
        val hash = hashBlock(blockHashedCount)
        hashes += hash
        return hash
    }

    /**
     * We can be sure that two docs are different when they don't start
     * with the same sequence of hash
     */
    fun differsFrom(other: Document): Boolean {
        return !hashes.startsWith(other.hashes) && !other.hashes.startsWith(hashes)
    }

    private fun hashBlock(blockIndex: Int): String {
        return FileUtils.hashBlock(relativePath.toFile(), blockIndex, HASH_BLOCK_SIZE)
    }
}