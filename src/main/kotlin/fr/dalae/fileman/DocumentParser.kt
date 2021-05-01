package fr.dalae.fileman

import fr.dalae.fileman.domain.Document
import fr.dalae.fileman.domain.SourceDir
import fr.dalae.fileman.domain.SourceFile
import java.nio.file.Path

class DocumentParser(private val sourceDir: SourceDir) {

    fun parse(relativePath: Path): Document {
        val originAbsPath = sourceDir.path.resolve(relativePath)
        val originAbsFile = originAbsPath.toFile()
        val relativeFile = relativePath.toFile()
        return Document(
            originAbsFile.lastModified(),
            originAbsFile.length(),
            "",
            0,
            relativePath,
            relativeFile.extension.toLowerCase()
        )
    }
}
