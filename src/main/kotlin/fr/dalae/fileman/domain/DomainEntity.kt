package fr.dalae.fileman.domain

import javax.persistence.*
import javax.persistence.GenerationType

import javax.persistence.GeneratedValue

@MappedSuperclass
abstract class DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    val id : Long = -1

    @Version
    protected var version: Long = -1
}
