package fr.dalae.fileman.domain

import fr.dalae.fileman.domain.Binary.Companion.HASH_BLOCK_SIZE
import javax.persistence.*

/**
 * A binary unit of data with the usual size, lastModified, extension.
 * But could be linked to multiple sourceFiles in the same or in multiple
 * sourceDir.
 */
@Entity
@Table(
    indexes = [
        Index(columnList = "lastModifiedEpochMs, size")
    ]
)
@SequenceGenerator(initialValue = 1, name = "generator", sequenceName = "documentSeq")
data class Binary(

    val lastModifiedEpochMs: Long,

    val size: Long,

    @Lob
    @Basic(fetch = FetchType.LAZY)
    var hashes: ArrayList<String> = arrayListOf(),

    @OneToMany
    val sourceFiles: MutableSet<SourceFile> = hashSetOf()

) : DomainEntity() {
    companion object {
        const val HASH_BLOCK_SIZE = 1024 * 10
    }

}

/**
 * A way of setting a file length by number of blocks of size HASH_BLOCK_SIZE
 */
fun Int.BLOCK(): Long {
    return this * HASH_BLOCK_SIZE.toLong()
}