package fr.dalae.fileman.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Tag(@Id val name: String)
