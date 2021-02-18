package fr.dalae.fileman.domain

import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
data class Origin(
    @ManyToOne(optional = false, cascade = [CascadeType.ALL])
    val source: Source,

    @NotEmpty
    val path: String,

    @ManyToOne(optional = false, cascade = [CascadeType.ALL])
    val document: Document
) : BaseEntity()
