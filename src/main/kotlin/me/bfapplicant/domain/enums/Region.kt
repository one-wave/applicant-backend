package me.bfapplicant.domain.enums

object Region {

    private val ALIASES = mapOf(
        "강원도" to "강원특별자치도",
        "전라북도" to "전북특별자치도"
    )

    private val REVERSE_ALIASES = ALIASES.entries.associate { (k, v) -> v to k }

    fun normalize(region: String): List<String> {
        val forward = ALIASES[region]
        if (forward != null) return listOf(region, forward)

        val reverse = REVERSE_ALIASES[region]
        if (reverse != null) return listOf(region, reverse)

        return listOf(region)
    }
}
