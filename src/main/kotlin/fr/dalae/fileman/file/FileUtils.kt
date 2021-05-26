package fr.dalae.fileman.file

import org.apache.commons.io.input.BoundedInputStream
import org.springframework.util.DigestUtils
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class FileUtils {

    companion object {
        val UNKNOWN_FILE = File("unknown-" + UUID.randomUUID())
        val UNKNOWN_PATH = UNKNOWN_FILE.toPath()
        val UNKNOWN_PATH_STRING = UNKNOWN_FILE.name

        fun md5(inputStream: InputStream, length: Long): String {
            val boundedInputStream = BoundedInputStream(inputStream, length)
            return DigestUtils.md5DigestAsHex(boundedInputStream)
        }

        fun toEpochMillis(dateTime: LocalDateTime): Long {
            return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }


        /**
         * Return the hash of the block (or partial last block) at the given index.
         * Returns empty if the requested block is out of range.
         */
        fun hashBlock(file: File, blockIndex: Int, blockSize: Int): String {
            val hashOffset = blockIndex * blockSize
            val block: ByteArray
            file.inputStream().use {
                it.skip(hashOffset.toLong())
                block = it.readNBytes(blockSize)
            }
            if (block.isEmpty()) return ""
            return DigestUtils.md5DigestAsHex(block)
        }

        /**
         * Provide an efficient way to iterate recursively through an entire directory using a Sequence.
         * NOTE : more efficient alternative might be to use NIO Files DirectoryStream<Path>
         * https://www.jmdoudoux.fr/java/dej/chap-nio2.htm
         */
        fun fileWalkingSequence(
            rootDir: Path,
            pathRelativeToRootDirectory: Boolean = true,
            followSymbolicLinks: Boolean = false
        ): Sequence<Path> {
            val originDir = rootDir.toFile()
            return originDir
                .walkTopDown()
                .onEnter {
                    followSymbolicLinks || !Files.isSymbolicLink(it.toPath())
                }
                .filter { it.isFile }
                .map { it.toPath() }
                .filter { followSymbolicLinks || !Files.isSymbolicLink(it) }
                .map {
                    if (pathRelativeToRootDirectory)
                        rootDir.relativize(it)
                    else
                        it
                }
        }
    }


}
