package fr.dalae.fileman.file

import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.math.min
import kotlin.random.Random

class NodeGenerator {
    companion object {
        const val MAX_SIZE = 10 * 1024 * 1024L
    }

    private val log = LoggerFactory.getLogger(javaClass)

    fun generateAll(root: Path, files: Stream<Node>) {
        files.filter {
            if (it.path.isAbsolute) throw IllegalArgumentException(
                "Cannot generate '$it'. Only relative path are allowed."
            )
            true
        }.forEach {
            it.withParentDir(root)
            generate(it)
        }
    }

    fun randomChange(fileNode: FileNode, position: Long, length: Int) {
        val absFile = fileNode.path.toFile()
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
    fun generate(node: Node) {
        when (node) {
            is SymLinkNode -> {
                generate(node)
            }
            is FileNode -> {
                generate(node)
            }
            else -> {
                throw IllegalArgumentException("Unknown kind of ${Node::class.simpleName}")
            }
        }
    }

    /**
     * Generate a file with the given properties.
     * Two options are proposed : deterministic (slow) and un-deterministic (fast)
     */
    fun generate(node: FileNode, randomSeed: Long = -1) {
        if (node.size > MAX_SIZE) throw IllegalArgumentException(
            "Cannot generate '$node'. Only files smaller than $MAX_SIZE bytes are allowed. "
        )
        ensureParentDirExists(node.path)
        val absFile = node.path.toFile()
        if (randomSeed < 0) {
            fastGenerate(absFile, node.size)
        } else {
            exactGenerate(absFile, node.size, randomSeed)
        }
        absFile.setLastModified(node.epochMillis)
        log.info("File '${absFile.path}' generated.")
    }

    private fun fastGenerate(file: File, size: Long) {
        RandomAccessFile(file, "rw").use {
            it.setLength(size)
        }
    }

    private fun exactGenerate(file: File, size: Long, randomSeed: Long) {
        val random = Random(randomSeed)
        FileOutputStream(file).use {
            var remainingCount = size
            while (remainingCount > 0) {
                val writeCount = min(remainingCount, 10480)
                it.write(random.nextBytes(writeCount.toInt()))
                remainingCount -= writeCount
            }
        }
    }

    fun generate(node: SymLinkNode) {
        ensureParentDirExists(node.path)
        val absSymbolicLink = node.symbolicLinkTarget.toAbsolutePath()
        Files.createSymbolicLink(node.path, node.symbolicLinkTarget)
        log.info("Symlink '${absSymbolicLink}' generated.")
    }

    private fun ensureParentDirExists(absPath: Path) {
        if (absPath.parent != null) {
            absPath.parent.toFile().mkdirs()
        }
    }


}
