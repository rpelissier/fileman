package fr.dalae.fileman.file

import java.nio.file.Path

open class Node(open var path: Path){
    open fun resolveInto(rootDir : Path) : Node{
        return Node(rootDir.resolve(path))
    }
}
