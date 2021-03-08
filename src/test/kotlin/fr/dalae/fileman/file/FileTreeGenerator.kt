package fr.dalae.fileman.file

import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

class FileTreeGenerator {
    companion object {
        const val MAX_SIZE = 10 * 1024 * 1024L
    }

    fun generate(root: Path, files: Stream<FileDescriptor>) {
        files.filter {
            if (it.path.isAbsolute) throw IllegalArgumentException(
                "Cannot generate '$it'. Only relative path are allowed."
            )
            if (it.size > MAX_SIZE) throw IllegalArgumentException(
                "Cannot generate '$it'. Only files smaller than $MAX_SIZE bytes are allowed. ")
            true
        }.map { it ->
            val absPath = root.resolve(it.path)
            val absSymbolicLink = it.symbolicLinkTarget?.let { root.resolve(it) }
            it.copy(path = absPath, symbolicLinkTarget = absSymbolicLink)
        }.forEach {
            createFile(it)
        }
    }

    private fun createFile(fd: FileDescriptor) {
        val file = fd.path.toFile()
        if (fd.path.parent != null) {
            fd.path.parent.toFile().mkdirs()
        }
        if (fd.symbolicLinkTarget != null) {
            Files.createSymbolicLink(fd.path, fd.symbolicLinkTarget.toAbsolutePath())
        } else {
            RandomAccessFile(file, "rw").use {
                it.setLength(fd.size)
            }
            file.setLastModified(fd.epochMillis)
        }
    }
}
