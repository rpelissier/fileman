package fr.dalae.fileman.file

import java.time.LocalDateTime
import java.time.ZoneId

class FileDateUtils {
    companion object {
        fun toEpochMillis(dateTime: LocalDateTime): Long {
            return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }
}
