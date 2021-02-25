package fr.dalae.fileman.domain

import java.io.File
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class FileConverter : AttributeConverter<File, String> {
    override fun convertToDatabaseColumn(attribute: File?): String {
        return attribute?.path ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): File {
        return if (dbData != null) File(dbData)
        else File("null")
    }
}
