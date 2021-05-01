package fr.dalae.fileman.domain

import java.io.Serializable
import java.nio.file.Path
import javax.persistence.*
import kotlin.io.path.name

@Entity
/**@Table(
indexes = [
Index(columnList = "name, lastModifiedEpochMs, size" )
]
)
 */
@IdClass(DocumentId::class)
data class Document(

    @Id
    val lastModifiedEpochMs: Long,

    @Id
    val size: Long,

    @Id
    @Column(columnDefinition = "varchar(32)")
    val md5: String = "",

    @Id
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

    val id: DocumentId
        get() = DocumentId(lastModifiedEpochMs, size, md5, hashCount)
}

// Composite key class must implement Serializable and have defaults.
data class DocumentId(
    val lastModifiedEpochMs: Long = 0,
    val size: Long = 0,
    val md5: String = "",
    val hashCount: Int = 0
) : Serializable