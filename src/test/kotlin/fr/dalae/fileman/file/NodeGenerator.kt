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
        }.map {
            it.resolveInto(root)
        }.forEach {
            when (it) {
                is FileNode -> generateExact(it)
                is SymLinkNode -> generate(it)
                else -> log.warn("Skipping unknown type of Node '${it.javaClass.simpleName}'.")
            }
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

    fun generateFastUnpredictable(node: FileNode) {
        generate(node) { file: File, size: Long ->
            generateUsingSetLength(file, size)
        }
    }

    fun generateExact(node: FileNode, randomSeed: Long = System.currentTimeMillis()) {
        generate(node) { file: File, size: Long ->
            generateWritingRandomSuite(file, size, randomSeed)
        }
    }

    /**
     * Generate a file with the given properties.
     * Two options are proposed : deterministic (slow) and un-deterministic (fast)
     */
    private fun generate(node: FileNode, generator: (File, Long) -> Unit) {
        if (node.size > MAX_SIZE) throw IllegalArgumentException(
            "Cannot generate '$node'. Only files smaller than $MAX_SIZE bytes are allowed. "
        )
        ensureParentDirExists(node.path)
        Files.deleteIfExists(node.path)
        val absFile = node.path.toFile()
        generator(absFile, node.size)
        absFile.setLastModified(node.epochMillis)
        log.info("File '${absFile.path}' generated.")
    }

    private fun generateUsingSetLength(file: File, size: Long) {
        RandomAccessFile(file, "rw").use {
            it.setLength(size)
        }
    }

    private fun generateWritingRandomSuite(file: File, size: Long, randomSeed: Long) {
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
        Files.deleteIfExists(node.path)
        Files.createSymbolicLink(node.path, node.symbolicLinkTarget.toAbsolutePath())
        log.info("Symlink '${node.symbolicLinkTarget}' generated.")
    }

    private fun ensureParentDirExists(absPath: Path) {
        if (absPath.parent != null) {
            absPath.parent.toFile().mkdirs()
        }
    }

}
