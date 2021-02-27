package fr.dalae.fileman.file

import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Predicate
import java.util.stream.Stream

class FileWalker {

    companion object {
        val FILE_ALWAYS_VALID = Predicate<Path> { true }
    }

    fun walk(directory: Path, isValidFile: Predicate<Path> = FILE_ALWAYS_VALID): Stream<Path> {
        val walk = Files.walk(directory)
        val isRegularFile = Predicate<Path> { path -> Files.isRegularFile(path) }
        return walk.filter(isRegularFile.and(isValidFile))
    }
}
