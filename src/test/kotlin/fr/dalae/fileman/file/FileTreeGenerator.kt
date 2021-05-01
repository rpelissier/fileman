package fr.dalae.fileman.file

import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

class FileTreeGenerator {
    companion object {
        const val MAX_SIZE = 10 * 1024 * 1024L
    }

    fun generate(root: Path, files: Stream<Node>) {
        files.filter {
            if (it.path.isAbsolute) throw IllegalArgumentException(
                "Cannot generate '$it'. Only relative path are allowed."
            )
            true
        }.forEach {
            createFile(root, it)
        }
    }

    private fun createFile(root: Path, fd: Node) {
        val absPath = root.resolve(fd.path).toAbsolutePath()
        if (absPath.parent != null) {
            absPath.parent.toFile().mkdirs()
        }
        when (fd) {
            is SymLinkNode -> {
                val absSymbolicLink = root.resolve(fd.symbolicLinkTarget).toAbsolutePath()
                Files.createSymbolicLink(absPath, absSymbolicLink)
            }
            is FileNode -> {
                if (fd.size > MAX_SIZE) throw IllegalArgumentException(
                    "Cannot generate '$fd'. Only files smaller than $MAX_SIZE bytes are allowed. "
                )
                val absFile = absPath.toFile()
                RandomAccessFile(absFile, "rw").use {
                    it.setLength(fd.size)
                }
                absFile.setLastModified(fd.epochMillis)
            }
            else -> {
                throw IllegalArgumentException("Unknown kind of ${Node::class.simpleName}")
            }
        }
    }
}
