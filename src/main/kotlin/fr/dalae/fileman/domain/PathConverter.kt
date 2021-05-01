package fr.dalae.fileman.domain

import java.nio.file.Path
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class PathConverter : AttributeConverter<Path, String> {
    override fun convertToDatabaseColumn(attribute: Path?): String {
        return attribute?.toString() ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): Path {
        return if (dbData != null) Path.of(dbData)
        else Path.of("no-path")
    }
}
