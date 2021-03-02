package fr.dalae.fileman.domain

import fr.dalae.fileman.file.FileUtils
import java.io.File
import java.io.Serializable
import java.nio.file.Path
import javax.persistence.*

@Entity
@IdClass(OriginId::class)
data class Origin(
    @Id
    val rootPath: File,

    @Id
    val relativePath: File,

    @ManyToOne(optional = false, cascade = [CascadeType.ALL])
    val document: Document
) {
    init {
        if (relativePath.isRooted) throw IllegalArgumentException("The path '$relativePath' should be relative, not absolute.")
    }

    val id: OriginId
        get() = OriginId(rootPath, relativePath)

    val absPath: Path
        get() = File(rootPath, relativePath.path).toPath()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Origin) return false

        if (rootPath != other.rootPath) return false
        if (relativePath != other.relativePath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rootPath.hashCode()
        result = 31 * result + relativePath.hashCode()
        return result
    }
}

// Composite key class must implement Serializable and have defaults.
data class OriginId(
    @Convert(converter = FileConverter::class)
    @Column(columnDefinition = "varchar(256)")
    val rootPath: File = FileUtils.UNKNOWN_FILE,
    @Convert(converter = FileConverter::class)
    @Column(columnDefinition = "varchar(512)")
    val relativePath: File = FileUtils.UNKNOWN_FILE
) : Serializable
