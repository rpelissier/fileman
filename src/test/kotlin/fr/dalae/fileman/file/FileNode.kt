package fr.dalae.fileman.file

import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class FileNode(
    override var path: Path,
    val date: LocalDateTime = LocalDateTime.now(),
    val size: Long = 0L
) : Node(path) {
    constructor(path: String, date: String, size: Long) :
            this(Path.of(path), date, size)

    constructor(path: Path, date: String, size: Long) :
            this(path,
                LocalDateTime.parse(date, DATE_TIME_FORMATTER),
                size)

    override fun resolveInto(rootDir: Path): FileNode {
        return FileNode(rootDir.resolve(path), date, size)
    }

    companion object {
        val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
    }

    val epochMillis: Long
        get() = FileUtils.toEpochMillis(date)
}
