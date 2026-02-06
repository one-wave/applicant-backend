package me.bfapplicant.domain.enums

enum class ReqEduc(val label: String, val level: Int) {
    ANY("무관", 0),
    HIGH_SCHOOL("고졸", 1),
    ASSOCIATE("초대졸", 2),
    BACHELOR("대졸", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: ANY
    }
}
