package me.bfapplicant.feature.workAssistant.service

import me.bfapplicant.domain.entity.WorkAssistantAgency
import me.bfapplicant.domain.repository.WorkAssistantAgencyRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.nio.charset.Charset

@Component
class AgencyDataLoader(
    private val repository: WorkAssistantAgencyRepository
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments) {
        if (repository.count() > 0) {
            log.info("Work assistant agencies already loaded ({} rows), skipping", repository.count())
            return
        }

        val resource = ClassPathResource("source/work_assistant_list.CSV")
        val agencies = resource.inputStream
            .bufferedReader(Charset.forName("EUC-KR"))
            .lineSequence()
            .drop(1)
            .filter { it.isNotBlank() }
            .map(::parseLine)
            .toList()

        repository.saveAll(agencies)
        log.info("Loaded {} work assistant agencies", agencies.size)
    }

    private fun parseLine(line: String): WorkAssistantAgency {
        val fields = splitCsv(line)
        val branch = fields[1].trim()
        val address = fields[4].trim()

        return WorkAssistantAgency(
            branch = branch,
            agencyName = fields[2].trim(),
            zipCode = fields[3].trim().ifBlank { null },
            address = address,
            bizNo = fields[5].trim().ifBlank { null },
            phone = fields.getOrNull(6)?.trim()?.ifBlank { null },
            region = extractRegion(address, branch)
        )
    }

    // Handles quoted fields containing commas
    private fun splitCsv(line: String): List<String> {
        val fields = mutableListOf<String>()
        val buf = StringBuilder()
        var inQuotes = false

        for (ch in line) {
            when {
                ch == '"' -> inQuotes = !inQuotes
                ch == ',' && !inQuotes -> {
                    fields.add(buf.toString())
                    buf.clear()
                }
                else -> buf.append(ch)
            }
        }
        fields.add(buf.toString())
        return fields
    }

    // Primary: address prefix, Fallback: branch name
    private fun extractRegion(address: String, branch: String): String {
        val source = address.ifBlank { branch }
        return REGION_PREFIXES.firstOrNull { (prefix, _) -> source.startsWith(prefix) }?.second
            ?: REGION_PREFIXES.firstOrNull { (prefix, _) -> branch.contains(prefix) }?.second
            ?: "기타"
    }

    companion object {
        private val REGION_PREFIXES = listOf(
            "서울" to "서울", "부산" to "부산", "대구" to "대구",
            "인천" to "인천", "광주" to "광주", "대전" to "대전",
            "울산" to "울산", "세종" to "세종", "경기" to "경기",
            "강원" to "강원", "충청북" to "충북", "충북" to "충북",
            "충청남" to "충남", "충남" to "충남", "전라북" to "전북",
            "전북" to "전북", "전라남" to "전남", "전남" to "전남",
            "경상북" to "경북", "경북" to "경북", "경상남" to "경남",
            "경남" to "경남", "제주" to "제주"
        )
    }
}
