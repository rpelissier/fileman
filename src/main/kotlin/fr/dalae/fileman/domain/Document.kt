import fr.dalae.fileman.domain.BaseEntity
import java.io.File
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.Table

@Entity
@Table(name = "document", schema = "fileman")
@Inheritance(strategy = InheritanceType.JOINED)
data class Document(
    val extension: String,
    val path: File
) : BaseEntity()
