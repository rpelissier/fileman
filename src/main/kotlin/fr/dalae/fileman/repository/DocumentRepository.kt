package fr.dalae.fileman.repository

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.DocumentId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : CrudRepository<Document, DocumentId>
