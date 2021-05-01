package fr.dalae.fileman.domain

import fr.dalae.fileman.file.FileUtils
import java.io.Serializable
import java.nio.file.Path
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass

@Entity
class SourceDir(
    path: Path
) : DomainEntity() {
    @Id
    @Column(columnDefinition = "varchar(512)")
    val pathString: String = path.toString()

    val path: Path
        get() = Path.of(pathString)

    companion object{
        val UNKNOWN = SourceDir(FileUtils.UNKNOWN_PATH)
    }
}
