package fr.dalae.fileman.domain.entity

import java.nio.file.Path
import javax.persistence.*

@Entity
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
