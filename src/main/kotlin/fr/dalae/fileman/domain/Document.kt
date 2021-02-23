package fr.dalae.fileman.domain

import java.io.File
import java.io.Serializable
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass

@Entity
@IdClass(DocumentId::class)
data class Document(
    @Id
    val name: String,
    @Id
    val lastModifiedEpochMs: Long,
    @Id
    val size: Long,

    val relativePath: File,
    val extension: String,
    @ElementCollection
    val tags: Set<String> = mutableSetOf()
)

// Composite key class must implement Serializable and have defaults.
class DocumentId(
    val name: String = "",
    val lastModifiedEpochMs: Long = 0L,
    val size: Long = 0L
) : Serializable
