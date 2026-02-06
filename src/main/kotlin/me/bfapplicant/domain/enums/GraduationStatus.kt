package me.bfapplicant.domain.enums

enum class GraduationStatus(val label: String) {
    GRADUATED("졸업"),
    ENROLLED("재학중"),
    ON_LEAVE("휴학"),
    DROPPED_OUT("중퇴"),
    EXPECTED("졸업예정");
}
