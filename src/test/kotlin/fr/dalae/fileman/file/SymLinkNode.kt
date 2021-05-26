package fr.dalae.fileman.file

import java.nio.file.Path

data class SymLinkNode(
    override var path: Path,
    var symbolicLinkTarget: Path
) : Node(path) {
    constructor(path: String, symbolicLinkTarget: String) :
            this(Path.of(path), Path.of(symbolicLinkTarget))

    override fun resolveInto(rootDir: Path): SymLinkNode {
        return SymLinkNode(rootDir.resolve(path),rootDir.resolve(symbolicLinkTarget))
    }
}
