import fr.dalae.fileman.domain.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "origin", schema = "fileman")
@Inheritance(strategy = InheritanceType.JOINED)
class Origin(
    val name: String,
    val key: String,

    @ManyToOne
    val document: Document
) : BaseEntity()
