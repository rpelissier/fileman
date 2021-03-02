package fr.dalae.fileman

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.Origin
import java.io.File

class OriginParser(val originDir: File) {

    fun parse(relativeFile: File): Origin {
        val doc = buildDoc(relativeFile)
        return Origin(originDir, relativeFile, doc)
    }

    private fun buildDoc(relativeFile: File): Document {
        val originAbsFile = File(originDir, relativeFile.path)
        val tags = buildTags(relativeFile)
        return Document(
            relativeFile.nameWithoutExtension,
            originAbsFile.lastModified(),
            originAbsFile.length(),
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
