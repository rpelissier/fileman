package fr.dalae.fileman.file

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Path

class HashTest {

    private val rootPath = Path.of("build/test")
    private val nodeGenerator = NodeGenerator()
    private val path1 = rootPath.resolve("file1.txt")
    private val path2 = rootPath.resolve("file2.txt")
    private val path3 = rootPath.resolve("file3.txt")

    @Test
    fun `Test hashNext() with file size is a multiple of block size`() {

        val fileNode = FileNode(path1, "2015-02-20T06:30:00.000", HashSuite.HASH_BLOCK_SIZE * 2L)
        nodeGenerator.generateExact(fileNode)

        val hash = HashSuite(path1)
        Assertions.assertNotEquals("", hash.hashNext())
        Assertions.assertNotEquals("", hash.hashNext())
        Assertions.assertEquals("", hash.hashNext())
        Assertions.assertEquals("", hash.hashNext())

    }

    @Test
    fun `Test hashNext() with file size is NOT a multiple of block size`() {

        val fileNode = FileNode(path1, "2015-02-20T06:30:00.000", (HashSuite.HASH_BLOCK_SIZE * 2.5).toLong())
        nodeGenerator.generateExact(fileNode)

        val hash = HashSuite(path1)
        Assertions.assertNotEquals("", hash.hashNext())
        Assertions.assertNotEquals("", hash.hashNext())
        Assertions.assertNotEquals("", hash.hashNext())
        Assertions.assertEquals("", hash.hashNext())
        Assertions.assertEquals("", hash.hashNext())
    }

    @Test
    fun `Test hash one block to prove difference`() {
        val randomSeed = System.currentTimeMillis()
        val date = "2015-02-20T06:30:00.000"
        val size = HashSuite.HASH_BLOCK_SIZE * 3L
        val fileNode = FileNode(path1, date, size)
        nodeGenerator.generateExact(fileNode, randomSeed)
        val fileNode2 = FileNode(path2, date, size)
        nodeGenerator.generateExact(fileNode2, randomSeed)
        val fileNode3 = FileNode(path3, date, size)
        nodeGenerator.generateExact(fileNode3)

        //Modify half the second block
        nodeGenerator.randomChange(fileNode2, HashSuite.HASH_BLOCK_SIZE.toLong(), HashSuite.HASH_BLOCK_SIZE / 2)

        val hash1 = HashSuite(fileNode.path)
        val hash2 = HashSuite(fileNode2.path)
        val hash3 = HashSuite(fileNode3.path)

        HashSuite.hashUntilProvedDifferent(hash1, hash2)
        Assertions.assertEquals(2, hash1.count)
        Assertions.assertEquals(2, hash2.count)

        HashSuite.hashUntilProvedDifferent(hash1, hash3)
        Assertions.assertEquals(2, hash1.count)
        Assertions.assertEquals(1, hash3.count)
    }
}