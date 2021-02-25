package fr.dalae.fileman.domain

import java.io.File
import java.io.Serializable
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
)

// Composite key class must implement Serializable and have defaults.
data class DocumentId(
    val name: String = "",
    val lastModifiedEpochMs: Long = 0L,
    val size: Long = 0L
) : Serializable
