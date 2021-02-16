package fr.dalae.fileman.domain

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    private val id: UUID = UUID.randomUUID()

    @Version
    private val version: Long? = null

    override fun equals(other: Any?) = when {
        this === other -> true
        javaClass != other?.javaClass -> false
        id != (other as BaseEntity).id -> false
        else -> true
    }

    override fun hashCode(): Int = id.hashCode()
}
