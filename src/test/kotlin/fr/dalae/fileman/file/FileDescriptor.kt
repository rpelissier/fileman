package fr.dalae.fileman.file

import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class FileDescriptor(val path: Path, val date: LocalDateTime, val size: Long) {
    constructor(path: String, date: String, size: Long) :
            this(Path.of(path), LocalDateTime.parse(date, DATE_TIME_FORMATTER), size)

    companion object {
        val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
    }

    val epochMillis: Long
        get() = FileDate.toEpochMillis(date)
}
