package fr.dalae.fileman.domain

import javax.persistence.*

/**
 * A binary unit of data with the usual size, lastModified, extension.
 * But could be linked to multiple sourceFiles in the same or in multiple
 * sourceDir.
 */
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

    val extension: String,

    @OneToMany(mappedBy = "document")
    val sourceFiles : MutableSet<SourceFile> = hashSetOf()

    ) : DomainEntity() {
    val hashCount: Int
        get() = hashes.size

}
