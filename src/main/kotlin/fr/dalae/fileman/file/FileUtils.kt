package fr.dalae.fileman.file

import java.io.File
import java.util.*

class FileUtils {

    companion object {
        val UNKNOWN_FILE = File("unknown-" + UUID.randomUUID())
        val UNKNOWN_PATH = UNKNOWN_FILE.toPath()
        val UNKNOWN_PATH_STRING = UNKNOWN_FILE.name
    }
}
