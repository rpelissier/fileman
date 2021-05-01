package fr.dalae.fileman.domain

import javax.persistence.*

@MappedSuperclass
abstract class DomainEntity {

    @Version
    protected var version: Long = -1
}
