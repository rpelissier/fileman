package fr.dalae.fileman.file

import java.nio.file.Path

class Hash(val path: Path, var blockHashes: String="") {

    companion object {
        val HASH_BLOCK_SIZE = 1024 * 10
        val HASH_BLOCK_N_CHAR = 32 //32 hexadecimal is 16 Bytes / 128 bits
    }

    val blockHashedCount: Int
        get() = blockHashes.length / HASH_BLOCK_N_CHAR

    /**
     * Hash one more block and update the hashes list.
     * Return empty string if no more block can be hashed
     */
    fun hashNext(): String {
        val hash = FileUtils.hashBlock(path.toFile(), blockHashedCount, HASH_BLOCK_SIZE)
        blockHashes += hash
        return hash
    }
}