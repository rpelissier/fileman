package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.Origin
import fr.dalae.fileman.domain.OriginId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OriginRepository : CrudRepository<Origin, OriginId>
