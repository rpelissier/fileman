package fr.dalae.fileman.file

import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class FileDescriptor(
    val path: Path,
    val date: LocalDateTime = LocalDateTime.now(),
    val size: Long = 0L,
    val symbolicLinkTarget: Path? = null
) {
    constructor(path: String, date: String, size: Long) :
            this(Path.of(path),
                LocalDateTime.parse(date, DATE_TIME_FORMATTER),
                size)

    constructor(path: String, symbolicLinkTarget: String) :
            this(Path.of(path), symbolicLinkTarget = Path.of(symbolicLinkTarget))

    companion object {
        val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
    }

    val epochMillis: Long
        get() = FileDateUtils.toEpochMillis(date)
}
