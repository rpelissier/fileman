package fr.dalae.fileman.domain

import java.nio.file.Path
import javax.persistence.*

@Entity
@SequenceGenerator(initialValue = 1, name = "generator", sequenceName = "sourceFileSeq")
@Table(
    indexes = [
        Index(columnList = "source_dir_id, relative_path", unique = true)
    ]
)
class SourceFile(
    @ManyToOne(optional = false)
    @JoinColumn(name="source_dir_id")
    val sourceDir: SourceDir,

    /**
     * Path relative to sourceDir
     */
    @Convert(converter = PathConverter::class)
    @Column(name="relative_path", columnDefinition = "varchar(512)")
    val relativePath: Path,

    @ManyToOne(optional = false)
    val document : Document,

    @ManyToMany
    val documentHistory: List<Document> = listOf()

) : DomainEntity() {


    init {
        if (relativePath.isAbsolute) throw IllegalArgumentException("The path '$relativePath' should be relative, not absolute.")
    }

    /**
     * Path including parent sourceDir path (not necessarily absolute)
     */
    val fullPath: Path
        get() = sourceDir.path.resolve(relativePath)
}

