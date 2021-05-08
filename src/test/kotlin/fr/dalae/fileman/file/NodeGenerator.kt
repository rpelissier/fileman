package fr.dalae.fileman.file

import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.random.Random

class NodeGenerator {
    companion object {
        const val MAX_SIZE = 10 * 1024 * 1024L
    }

    fun generateAll(root: Path, files: Stream<Node>) {
        files.filter {
            if (it.path.isAbsolute) throw IllegalArgumentException(
                "Cannot generate '$it'. Only relative path are allowed."
            )
            true
        }.forEach {
            generate(root, it)
        }
    }

    fun randomChange(root: Path, fileNode: FileNode, position: Long, length: Int) {
        val absPath = root.resolve(fileNode.path).toAbsolutePath()
        val absFile = absPath.toFile()
        RandomAccessFile(absFile, "rw").use {
            it.seek(position)
            it.write(Random.Default.nextBytes(length))
        }
        absFile.setLastModified(fileNode.epochMillis)
    }

    /**
     * Generates a Node, creates the parent dirs if necessary.
     * Overwrites existing file.
     */
    fun generate(root: Path, node: Node) {
        val absPath = root.resolve(node.path).toAbsolutePath()
        if (absPath.parent != null) {
            absPath.parent.toFile().mkdirs()
        }
        when (node) {
            is SymLinkNode -> {
                val absSymbolicLink = root.resolve(node.symbolicLinkTarget).toAbsolutePath()
                Files.createSymbolicLink(absPath, absSymbolicLink)
            }
            is FileNode -> {
                if (node.size > MAX_SIZE) throw IllegalArgumentException(
                    "Cannot generate '$node'. Only files smaller than $MAX_SIZE bytes are allowed. "
                )
                val absFile = absPath.toFile()
                RandomAccessFile(absFile, "rw").use {
                    it.setLength(node.size)
                }
                absFile.setLastModified(node.epochMillis)
            }
            else -> {
                throw IllegalArgumentException("Unknown kind of ${Node::class.simpleName}")
            }
        }
    }
}
