package me.bfapplicant.domain.enums

object CareerParser {

    private val PATTERN = Regex("""(\d+)년(\d*)개월""")

    fun toMonths(raw: String): Int {
        if (raw == "무관") return 0

        val match = PATTERN.find(raw) ?: return 0
        val years = match.groupValues[1].toIntOrNull() ?: 0
        val months = match.groupValues[2].toIntOrNull() ?: 0
        return years * 12 + months
    }
}
