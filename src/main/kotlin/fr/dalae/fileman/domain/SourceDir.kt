package fr.dalae.fileman.domain

import java.nio.file.Path
import javax.persistence.*

@Entity
@SequenceGenerator(initialValue = 1, name = "generator", sequenceName = "sourceDirSeq")
@Table(
    indexes = [
        Index(columnList = "path", unique = true)
    ]
)
class SourceDir(
    @Convert(converter = PathConverter::class)
    @Column(columnDefinition = "varchar(512)")
    val path: Path
) : DomainEntity()
