package fr.dalae.fileman.domain.entity

import java.util.*
import javax.persistence.*
import javax.persistence.GenerationType

import javax.persistence.GeneratedValue

@MappedSuperclass
abstract class DomainEntity {

    @Id
    val id : String = UUID.randomUUID().toString()

    @Version
    protected var version: Long = -1
}
