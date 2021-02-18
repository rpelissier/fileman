package fr.dalae.fileman.domain

import java.io.File
import java.util.*
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.Table

@Entity
@Table(name = "document", schema = "fileman")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
data class Document(
    val extension: String,
    val path: File,
    val lastModified: Date,
    val size: Long
) : BaseEntity()
