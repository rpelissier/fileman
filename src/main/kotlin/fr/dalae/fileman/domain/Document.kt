package fr.dalae.fileman.domain

import java.io.File
import java.io.Serializable
import java.nio.file.Path
import javax.persistence.*

@Entity
@IdClass(DocumentId::class)
data class Document(
    @Id
    val name: String,
    @Id
    val lastModifiedEpochMs: Long,
    @Id
    val size: Long,

    @Convert(converter = FileConverter::class)
    @Column(columnDefinition = "varchar(512)")
    val relativePath: File,
    val extension: String,
    @ElementCollection
    val tags: Set<String> = mutableSetOf()
) {
    val id: DocumentId
        get() = DocumentId(name, lastModifiedEpochMs, size)

    fun resolveInto(storageDir: Path): Path {
        return storageDir.resolve(relativePath.toPath())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Document) return false

        if (name != other.name) return false
        if (lastModifiedEpochMs != other.lastModifiedEpochMs) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + lastModifiedEpochMs.hashCode()
        result = 31 * result + size.hashCode()
        return result
    }
}

// Composite key class must implement Serializable and have defaults.
data class DocumentId(
    val name: String = "",
    val lastModifiedEpochMs: Long = 0L,
    val size: Long = 0L
) : Serializable
