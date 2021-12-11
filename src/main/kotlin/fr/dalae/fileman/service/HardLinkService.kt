package fr.dalae.fileman.service

import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object HardLinkService {

    fun hardLinkToStorage(storageDir: Path, existingPath: Path): Path {
        val uuid = UUID.randomUUID().toString()
        //To avoid storing 100,000 in a flat directory structure
        //we use the first two pair of hexa to create 2 levels.
        val relativePath = Path.of(
            uuid.substring(0..1),
            uuid.substring(2..3),
            uuid.substring(4)
        )
        val absPath = storageDir.resolve(relativePath)
        absPath.parent.toFile().mkdirs()
        Files.createLink(absPath, existingPath);
        return relativePath
    }
}