package fr.dalae.fileman.domain

import javax.persistence.*

@Entity
@Table(name = "origin", schema = "fileman")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
data class Origin(
    val sourceId: String,
    val path: String,

    @ManyToOne
    val document: Document
) : BaseEntity()
