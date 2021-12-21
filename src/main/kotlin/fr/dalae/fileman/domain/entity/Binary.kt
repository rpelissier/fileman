package fr.dalae.fileman.domain.entity

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
data class Binary(

    val lastModifiedEpochMs: Long,

    val size: Long,

    @Lob
    @Basic(fetch = FetchType.LAZY)
    var hashes: ArrayList<String> = arrayListOf(),

    @OneToMany
    val sourceFiles: MutableSet<SourceFile> = hashSetOf()

) : DomainEntity()