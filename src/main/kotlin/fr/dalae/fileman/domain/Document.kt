package fr.dalae.fileman.domain

import fr.dalae.fileman.file.HashSuite
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
    val storageRelativePath: Path,

    val extension: String,

    ) : DomainEntity() {
    val hashCount: Int
        get() = hashes.length / HashSuite.HASH_BLOCK_N_CHAR

}
