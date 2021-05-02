package fr.dalae.fileman.domain

import java.nio.file.Path
import javax.persistence.*

@Entity
@Table(
    indexes = [
        Index(columnList = "lastModifiedEpochMs, size, hashCount, md5", unique = true)
    ]
)
@SequenceGenerator(initialValue = 1, name = "generator", sequenceName = "documentSeq")
data class Document(

    val lastModifiedEpochMs: Long,

    val size: Long,

    @Column(columnDefinition = "varchar(32)")
    val md5: String = "",

    val hashCount: Int = 0,

    @Convert(converter = PathConverter::class)
    @Column(columnDefinition = "varchar(512)")
    val relativePath: Path,

    val extension: String,

    ) : DomainEntity() {
    fun resolveInto(storageDir: Path): Path {
        return storageDir.resolve(relativePath)
    }

    val name: String
        get() = relativePath.fileName.toString()
}