package me.bfapplicant.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    enumAsRef = true,
    description = """
졸업 상태:
* GRADUATED - 졸업
* ENROLLED - 재학중
* ON_LEAVE - 휴학
* DROPPED_OUT - 중퇴
* EXPECTED - 졸업예정"""
)
enum class GraduationStatus(val label: String) {
    GRADUATED("졸업"),
    ENROLLED("재학중"),
    ON_LEAVE("휴학"),
    DROPPED_OUT("중퇴"),
    EXPECTED("졸업예정");
}
