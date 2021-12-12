package fr.dalae.fileman.file

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Path

class HashUtilsTest {

    private val rootPath = Path.of("build/test")
    private val nodeGenerator = NodeGenerator()
    private val path1 = rootPath.resolve("file1.txt")
    private val path2 = rootPath.resolve("file2.txt")
    private val path3 = rootPath.resolve("file3.txt")

    @Test
    fun `Test hashNext() with file size is a multiple of block size`() {

        val fileNode = FileNode(path1, "2015-02-20T06:30:00.000", HashUtils.HASH_BLOCK_SIZE * 2L)
        nodeGenerator.generateExact(fileNode)

        val file = path1.toFile()
        val hashes = mutableListOf<String>()
        Assertions.assertNotEquals("", HashUtils.hashNext(file, hashes))
        Assertions.assertNotEquals("", HashUtils.hashNext(file, hashes))
        Assertions.assertEquals("", HashUtils.hashNext(file, hashes))
        Assertions.assertEquals("", HashUtils.hashNext(file, hashes))

    }

    @Test
    fun `Test hashNext() with file size is NOT a multiple of block size`() {

        val fileNode = FileNode(path1, "2015-02-20T06:30:00.000", (HashUtils.HASH_BLOCK_SIZE * 2.5).toLong())
        nodeGenerator.generateExact(fileNode)

        val file = path1.toFile()
        val hashes = mutableListOf<String>()
        Assertions.assertNotEquals("", HashUtils.hashNext(file, hashes))
        Assertions.assertNotEquals("", HashUtils.hashNext(file, hashes))
        Assertions.assertNotEquals("", HashUtils.hashNext(file, hashes))
        Assertions.assertEquals("", HashUtils.hashNext(file, hashes))
        Assertions.assertEquals("", HashUtils.hashNext(file, hashes))
    }

    @Test
    fun `Test hash one block to prove difference`() {
        val randomSeed = System.currentTimeMillis()
        val date = "2015-02-20T06:30:00.000"
        val size = HashUtils.HASH_BLOCK_SIZE * 3L
        val fileNode = FileNode(path1, date, size)
        nodeGenerator.generateExact(fileNode, randomSeed)
        val fileNode2 = FileNode(path2, date, size)
        nodeGenerator.generateExact(fileNode2, randomSeed)
        val fileNode3 = FileNode(path3, date, size)
        nodeGenerator.generateExact(fileNode3)

        //Modify half the second block
        nodeGenerator.randomChange(fileNode2, HashUtils.HASH_BLOCK_SIZE.toLong(), HashUtils.HASH_BLOCK_SIZE / 2)

        val file1 = path1.toFile()
        val hashes1 = mutableListOf<String>()
        val file2 = path2.toFile()
        val hashes2 = mutableListOf<String>()
        val file3 = path3.toFile()
        val hashes3 = mutableListOf<String>()


        HashUtils.hashUntilProvedDifferent(file1, hashes1, file2, hashes2)
        Assertions.assertEquals(2, hashes1.size)
        Assertions.assertEquals(2, hashes2.size)

        HashUtils.hashUntilProvedDifferent(file1, hashes1, file3, hashes3)
        Assertions.assertEquals(2, hashes1.size)
        Assertions.assertEquals(1, hashes3.size)
    }

    @Test
    fun `Test prove equality on huge file`() {

        val randomSeed = System.currentTimeMillis()
        val date = "2015-02-20T06:30:00.000"
        val size = 1000.MB()
        NodeGenerator.MAX_SIZE = size
        val fileNode = FileNode(path1, date, size)
        nodeGenerator.generateExact(fileNode, randomSeed)
        val fileNode2 = FileNode(path2, date, size)
        nodeGenerator.generateExact(fileNode2, randomSeed)

        val file1 = path1.toFile()
        val hashes1 = mutableListOf<String>()
        val file2 = path2.toFile()
        val hashes2 = mutableListOf<String>()

        val different = HashUtils.hashUntilProvedDifferent(file1, hashes1, file2, hashes2)

        Assertions.assertFalse(different)
    }

    @Test
    fun `Test prove difference when file starts with the other`() {

        val randomSeed = System.currentTimeMillis()
        val date = "2015-02-20T06:30:00.000"
        val size = 10.BLOCK()
        val fileNode = FileNode(path1, date, size)
        nodeGenerator.generateExact(fileNode, randomSeed)
        val fileNode2 = FileNode(path2, date, size+1)
        nodeGenerator.generateExact(fileNode2, randomSeed)

        val file1 = path1.toFile()
        val hashes1 = mutableListOf<String>()
        val file2 = path2.toFile()
        val hashes2 = mutableListOf<String>()

        val different = HashUtils.hashUntilProvedDifferent(file1, hashes1, file2, hashes2)

        Assertions.assertTrue(different)
        println("" + hashes1.size + " <> " + hashes2.size)


        //Second attempts switching files
        val hashes1Bis = mutableListOf<String>()
        val hashes2Bis = mutableListOf<String>()
        Assertions.assertTrue(HashUtils.hashUntilProvedDifferent( file2, hashes2Bis, file1, hashes1Bis))
        println("" + hashes1Bis.size + " <> " + hashes2Bis.size)
    }
}