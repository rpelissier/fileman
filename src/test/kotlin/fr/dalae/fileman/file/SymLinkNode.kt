package fr.dalae.fileman.file

import java.nio.file.Path

data class SymLinkNode(
    override val path: Path,
    val symbolicLinkTarget: Path
) : Node(path) {
    constructor(path: String, symbolicLinkTarget: String) :
            this(Path.of(path), Path.of(symbolicLinkTarget))
}
