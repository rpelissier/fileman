package fr.dalae.fileman

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.repository.DocumentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DocumentService {

    @Autowired
    lateinit var docRepository : DocumentRepository

    fun persist(newDoc : Document) : Document{
        val siblingDocs = docRepository.findByLastModifiedEpochMsAndSizeOrderByHashedLengthAsc(newDoc.lastModifiedEpochMs, newDoc.size)

        if(siblingDocs.isEmpty()){
            return docRepository.save(newDoc)
        }

        //TODO
        // se souvenir que les siblings sont déjà différents entre eux
        // ALGO
        // pour chaque paire doc + sibling
        // tant que pas différents et taille inégale, faire grossir le plus petit
        // tant que pas différents et taille égale, faire grossir les deux
        // si taille max et pas différents alors égaux (et donc tous les autres sont différents)


    }


    fun sortOutDifferentDoc(doc : Document, siblingDocs : MutableList<Document>, siblingDifferentDocs : MutableList<Document>){
        val differentSiblingDocs = mutableListOf<Document>()
        siblingDocs.forEach { sibling ->
            if(sibling.differsFrom(doc))
                differentSiblingDocs.add(sibling)
        }
        siblingDocs.removeAll(differentSiblingDocs)
        siblingDifferentDocs.addAll(differentSiblingDocs)
    }



}