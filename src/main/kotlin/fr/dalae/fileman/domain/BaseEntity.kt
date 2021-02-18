package fr.dalae.fileman.domain

import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {
    @Id
    var id: String = UUID.randomUUID().toString()

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
