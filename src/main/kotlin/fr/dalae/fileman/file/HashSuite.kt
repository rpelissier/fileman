package fr.dalae.fileman.file

import java.nio.file.Path
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class HashSuite(val path: Path, var blockHashes: ArrayList<String> = arrayListOf()) {

    companion object {
        val HASH_BLOCK_SIZE = 1024 * 10
        val HASH_BLOCK_N_CHAR = 32 //32 hexadecimal = 16 Bytes = 128 bits

        /**
         * Compute the necessary hash to demonstrate difference or default to equality
         */
        fun hashUntilProvedDifferent(suite1: HashSuite, suite2: HashSuite): Boolean {
            if (areProvedDifferent(suite1, suite2)) return true

            //If we are here every existing hash are equals
            while (true) {
                // More hash needed
                val hash1: String
                val hash2: String
                if (suite1.count < suite2.count) {
                    hash1 = suite1.hashNext()
                    hash2 = suite2.blockHashes[suite1.blockHashes.lastIndex]
                    //If the smallest file is over, they are different.
                    if (hash1.isEmpty()) return true
                } else if (suite1.count > suite2.count) {
                    hash2 = suite2.hashNext()
                    hash1 = suite1.blockHashes[suite2.blockHashes.lastIndex]
                    //If the smallest file is over, they are different.
                    if (hash2.isEmpty()) return true
                } else {
                    //File have the same number of block. We need two extra blocks to progress
                    hash1 = suite1.hashNext()
                    hash2 = suite2.hashNext()
                    //If both files are over, they are equals.
                    if (hash1.isEmpty() && hash2.isEmpty()) return false
                }

                //If hashes are different, they are different.
                if (hash1 != hash2) return true

                //If we are here we need another run
            }
        }


        fun areProvedDifferent(
            suite1: HashSuite,
            suite2: HashSuite
        ): Boolean {
            for (i in 0 until min(suite1.count, suite2.count)) {
                if (suite1.blockHashes[i] != suite2.blockHashes[i])
                    return true
            }
            return false
        }
    }

    val count: Int
        get() = blockHashes.count()

    /**
     * Hash one more block and update the hashes list.
     * Return empty string if no more block can be hashed
     */
    fun hashNext(): String {
        val hash = FileUtils.hashBlock(path.toFile(), count, HASH_BLOCK_SIZE)
        if (hash.isNotEmpty()) blockHashes.add(hash)
        return hash
    }
}

/**
 * A way of setting a file length by number of blocks of size HASH_BLOCK_SIZE
 */
fun Int.BLOCK(): Long {
    return this * HashSuite.HASH_BLOCK_SIZE.toLong()
}