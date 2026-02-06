package me.bfapplicant.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    enumAsRef = true,
    description = """
학위 수준:
* UNDER_HIGH_SCHOOL - 중졸이하
* HIGH_SCHOOL - 고졸
* ASSOCIATE - 초대졸
* BACHELOR - 대졸
* MASTER - 석사
* DOCTOR - 박사"""
)
enum class EducLevel(val label: String, val level: Int) {
    UNDER_HIGH_SCHOOL("중졸이하", 0),
    HIGH_SCHOOL("고졸", 1),
    ASSOCIATE("초대졸", 2),
    BACHELOR("대졸", 3),
    MASTER("석사", 4),
    DOCTOR("박사", 5);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: UNDER_HIGH_SCHOOL
    }
}
