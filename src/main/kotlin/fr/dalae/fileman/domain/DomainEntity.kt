package fr.dalae.fileman.domain

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
