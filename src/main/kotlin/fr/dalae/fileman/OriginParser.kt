package fr.dalae.fileman

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.Origin
import java.io.File

class OriginParser {

    fun parse(rootDirectory: File, relativeFile: File): Origin {
        if (relativeFile.isRooted) throw IllegalArgumentException("The path '$relativeFile' should be relative, not absolute.")

        val doc = buildDoc(rootDirectory, relativeFile)
        return Origin(rootDirectory, relativeFile, doc)
    }

    private fun buildDoc(rootDirectory: File, relativeFile: File): Document {
        val absFile = File(rootDirectory, relativeFile.path)
        val tags = buildTags(relativeFile)
        return Document(
            relativeFile.nameWithoutExtension,
            absFile.lastModified(),
            absFile.length(),
            relativeFile,
            relativeFile.extension.toLowerCase(),
            tags
        )
    }

    private fun buildTags(relativeFile: File): Set<String> {
        val names = parseNames(relativeFile)
        val tags = mutableSetOf<String>()
        names.forEach { name -> tags.add(name) }
        return tags
    }

    private fun parseNames(file: File): Set<String> {
        val names = mutableSetOf<String>()
        val parentFile = file.parentFile
        if (parentFile != null) parseNames(file.parentFile, names)
        return names
    }

    private fun parseNames(file: File, names: MutableSet<String>) {

        val name = file.name.trim()
        if (name.isNotBlank()) names.add(name)
        val parent = file.parentFile ?: return
        return parseNames(parent, names)
    }
}
