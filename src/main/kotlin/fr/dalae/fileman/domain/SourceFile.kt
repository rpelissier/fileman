package fr.dalae.fileman.domain

import java.io.Serializable
import java.nio.file.Path
import javax.persistence.*

@Entity
@IdClass(SourceFileId::class)
class SourceFile(
    @ManyToOne(optional = false)
    @MapsId
    @JoinColumn(name = "sourceDirPathString")
    // See https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/
    val sourceDir: SourceDir,

    relativePath: Path,

    @ManyToOne(optional = false)
    val document : Document,

    @ManyToMany
    val documentHistory: List<Document> = listOf(),

) : DomainEntity() {


    init {
        if (relativePath.isAbsolute) throw IllegalArgumentException("The path '$relativePath' should be relative, not absolute.")
    }

    @Id
    @Column(columnDefinition = "varchar(512)")
    val sourceDirPathString : String = sourceDir.path.toString()

    @Id
    @Column(columnDefinition = "varchar(512)")
    val relativePathString: String = relativePath.toString()

    /**
     * Path relative to sourceDir
     */
    val relativePath: Path
        get() = Path.of(relativePathString)

    /**
     * Path including sourceDir path (not necessarily absolute)
     */
    val fullPath: Path
        get() = sourceDir.path.resolve(relativePathString)

    val id: SourceFileId
        get() = SourceFileId(sourceDir.toString(), relativePathString)
}

// Composite key class must implement Serializable and have defaults.
data class SourceFileId(
    val sourceDirPathString : String ="",
    val relativePathString: String = ""
) : Serializable
