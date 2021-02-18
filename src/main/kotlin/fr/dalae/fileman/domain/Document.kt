package fr.dalae.fileman.domain

import java.io.File
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
data class Document(
    val extension: String,
    @NotEmpty
    val path: File,
    val lastModified: Date,
    val size: Long,

    @ManyToMany(cascade = [CascadeType.ALL])
    val tags: Set<Tag> = mutableSetOf()
) : BaseEntity()
