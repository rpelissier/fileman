package fr.dalae.fileman.domain

import java.io.File
import java.nio.file.Path
import javax.persistence.*
import kotlin.io.path.extension

/**
 * A pointer to a file within a source dir.
 */
@Entity(name = "source_file")
@Table(
    indexes = [
        Index(columnList = "lastModifiedEpochMs, size"),
        Index(columnList = "extension"),
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

    var lastModifiedEpochMs: Long,

    var size: Long,

    val extension: String,

    ) : DomainEntity() {

    companion object{
        fun create(sourceDir: SourceDir, relativePath: Path) : SourceFile{
            val file = sourceDir.path.resolve(relativePath).toFile()
            val lastModified = file.lastModified()
            val size = file.length()
            val ext = file.extension
            return SourceFile(sourceDir, relativePath, lastModified, size, ext)
        }
    }

    init {
        if (relativePath.isAbsolute) throw IllegalArgumentException("The path '$relativePath' should be relative, not absolute.")
    }

    /**
     * Path including parent sourceDir path (not necessarily absolute)
     */
    val file: File
        get() = sourceDir.path.resolve(relativePath).toFile()
}

