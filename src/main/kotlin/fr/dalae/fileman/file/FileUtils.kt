package fr.dalae.fileman.file

import fr.dalae.fileman.domain.Document
import org.apache.commons.io.input.BoundedInputStream
import org.springframework.util.DigestUtils
import java.io.File
import java.io.InputStream
import java.math.BigInteger
import java.security.DigestInputStream
import java.security.MessageDigest
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


        /**
         * Return the hash of the block (or partial last block) at the given index.
         * Returns empty if the requested block is out of range.
         */
        fun hashBlock(file: File, blockIndex: Int, blockSize: Int): String {
            val hashOffset = blockIndex * blockSize
            var block = ByteArray(blockSize)
            val nRead: Int
            file.inputStream().use {
                nRead = it.readNBytes(block, hashOffset, blockSize)
            }
            if (nRead <= 0) return ""

            if (nRead < block.size) {
                block = block.copyOf(nRead)
            }
            return DigestUtils.md5DigestAsHex(block)
        }
    }


}
