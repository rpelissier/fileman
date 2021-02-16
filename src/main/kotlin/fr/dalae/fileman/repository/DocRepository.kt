package fr.dalae.fileman.repository

import Document
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface DocRepository : JpaRepository<Document, UUID>
