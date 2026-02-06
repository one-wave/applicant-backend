package me.bfapplicant.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import me.bfapplicant.feature.application.dto.ResumeSnapshot
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule

@Converter(autoApply = false)
class ResumeSnapshotConverter : AttributeConverter<ResumeSnapshot, String> {

    companion object {
        private val mapper = JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .build()
    }

    override fun convertToDatabaseColumn(attribute: ResumeSnapshot): String =
        mapper.writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String): ResumeSnapshot =
        mapper.readValue(dbData, ResumeSnapshot::class.java)
}
