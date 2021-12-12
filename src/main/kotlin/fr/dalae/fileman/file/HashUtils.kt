package fr.dalae.fileman.file

import org.springframework.util.DigestUtils
import java.io.File
import kotlin.math.min

object HashUtils {

    const val HASH_BLOCK_SIZE = 1024 * 10

    /**
     * Compute the necessary hash to demonstrate difference or default to equality
     */
    fun hashUntilProvedDifferent(
        file1: File,
        hashes1: MutableList<String>,
        file2: File,
        hashes2: MutableList<String>
    ): Boolean {
        if (isProvedDifferent(hashes1, hashes2)) return true

        //If we are here every existing hash are equals
        while (true) {
            // More hash needed
            val thisHash: String
            val otherHash: String
            if (hashes1.size < hashes2.size) {
                thisHash = hashNext(file1, hashes1)
                otherHash = hashes2[hashes1.lastIndex]
                //If the smallest file is over, they are different.
                if (thisHash.isEmpty()) return true
            } else if (hashes1.size > hashes2.size) {
                otherHash = hashNext(file2, hashes2)
                thisHash = hashes1[hashes2.lastIndex]
                //If the smallest file is over, they are different.
                if (otherHash.isEmpty()) return true
            } else {
                //File have the same number of block. We need two extra blocks to progress
                thisHash = hashNext(file1, hashes1)
                otherHash = hashNext(file2, hashes2)
                //If both files are over, they are equals.
                if (thisHash.isEmpty() && otherHash.isEmpty()) return false
            }

            //If hashes are different, they are different.
            if (thisHash != otherHash) return true

            //If we are here we need another run
        }
    }


    private fun isProvedDifferent(bin1: MutableList<String>, bin2: MutableList<String>): Boolean {
        for (i in 0 until min(bin1.size, bin2.size)) {
            if (bin1[i] != bin2[i])
                return true
        }
        return false
    }


    /**
     * Hash one more block and update the hashes list.
     * Return empty string if no more block can be hashed
     */
    fun hashNext(file: File, hashes: MutableList<String>): String {
        val hash = hashBlock(file, hashes.size, HASH_BLOCK_SIZE)
        if (hash.isNotEmpty()) hashes.add(hash)
        return hash
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
}