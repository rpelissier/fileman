package fr.dalae.fileman.file

import java.nio.file.Path

open class Node(open var path: Path){
    fun withParentDir(rootDir : Path){
        path = rootDir.resolve(path)
    }
}
