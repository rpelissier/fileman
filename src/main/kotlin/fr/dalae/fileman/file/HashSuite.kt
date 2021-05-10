package fr.dalae.fileman.file

import java.nio.file.Path

class HashSuite(val path: Path, var blockHashes: String="") {

    companion object {
        val HASH_BLOCK_SIZE = 1024 * 10
        val HASH_BLOCK_N_CHAR = 32 //32 hexadecimal = 16 Bytes = 128 bits

        /**
         * Compute the necessary hash to demonstrate difference or default to equality
         */
        fun hashUntilProvedDifferent(suite1: HashSuite, suite2: HashSuite): Boolean {
            if (areProvedDifferent(suite1, suite2)) return true
            // More hash needed
            val nextHash = if (suite1.count < suite2.count) suite1.hashNext() else suite2.hashNext()
            //No more hash available means equality
            if (nextHash == "") return false
            //New iteration as a new hash is available
            return hashUntilProvedDifferent(suite1, suite2)
        }

        /**
         * Document are for sure differents if current hashes don't start
         * with the same sequence
         */
        fun areProvedDifferent(pHashSuite1: HashSuite, pHashSuite2: HashSuite): Boolean {
            return !pHashSuite1.blockHashes.startsWith(pHashSuite2.blockHashes) && !pHashSuite2.blockHashes.startsWith(pHashSuite1.blockHashes)
        }
    }

    val count: Int
        get() = blockHashes.length / HASH_BLOCK_N_CHAR

    /**
     * Hash one more block and update the hashes list.
     * Return empty string if no more block can be hashed
     */
    fun hashNext(): String {
        val hash = FileUtils.hashBlock(path.toFile(), count, HASH_BLOCK_SIZE)
        blockHashes += hash
        return hash
    }
}

/**
 * A way of setting a file length by number of blocks of size HASH_BLOCK_SIZE
 */
fun Int.BLOCK(): Long {
    return this * HashSuite.HASH_BLOCK_SIZE.toLong()
}