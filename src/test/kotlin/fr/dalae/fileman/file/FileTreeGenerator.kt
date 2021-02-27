package fr.dalae.fileman.file

import java.io.RandomAccessFile
import java.nio.file.Path
import java.util.stream.Stream

class FileTreeGenerator {
    companion object {
        val MAX_SIZE = 10 * 1024 * 1024L
    }

    fun generate(root: Path, files: Stream<FileDescriptor>) {
        files.filter {
            if (it.path.isAbsolute) throw IllegalArgumentException(
                "Cannot generate '$it'. Only relative path are allowed."
            )
            if (it.size > MAX_SIZE) throw IllegalArgumentException(
                "Cannot generate '$it'. Only files smaller than $MAX_SIZE bytes are allowed. ")
            true
        }.map {
            val absPath = root.resolve(it.path)
            it.copy(path = absPath)
        }.forEach {
            createFile(it)
        }
    }

    private fun createFile(fd: FileDescriptor) {
        val file = fd.path.toFile()
        RandomAccessFile(file, "rw").use {
            it.setLength(fd.size)
        }
        file.setLastModified(fd.epochMillis)
    }
}
