package fr.dalae.fileman.domain

import fr.dalae.fileman.file.HashSuite
import java.nio.file.Path
import javax.persistence.*

@Entity
@Table(
    indexes = [
        Index(columnList = "lastModifiedEpochMs, size"),
        Index(columnList = "extension")
    ]
)
@SequenceGenerator(initialValue = 1, name = "generator", sequenceName = "documentSeq")
data class Document(

    val lastModifiedEpochMs: Long,

    val size: Long,

    @Lob
    @Basic(fetch = FetchType.LAZY)
    var hashes: ArrayList<String> = arrayListOf(),

    @Convert(converter = PathConverter::class)
    @Column(columnDefinition = "varchar(512)")
    val storageRelativePath: Path,

    val extension: String,

    @OneToMany(mappedBy = "document")
    val sourcefiles : MutableSet<SourceFile> = hashSetOf()

    ) : DomainEntity() {
    val hashCount: Int
        get() = hashes.size



}
