package me.bfapplicant.domain.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import me.bfapplicant.domain.entity.JobPost
import me.bfapplicant.domain.entity.QCompany.company
import me.bfapplicant.domain.entity.QJobPost.jobPost
import me.bfapplicant.domain.enums.Region
import me.bfapplicant.domain.enums.SortType
import me.bfapplicant.feature.jobSearch.dto.JobSearchFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Repository
class JobPostQueryRepository(private val queryFactory: JPAQueryFactory) {

    fun search(
        filter: JobSearchFilter,
        envExcludes: EnvExcludes,
        educExcludes: List<String>?,
        careerMonths: Int?,
        pageable: Pageable
    ): Page<JobPost> {
        val where = buildConditions(filter, envExcludes, educExcludes, careerMonths)

        val query = queryFactory
            .selectFrom(jobPost)
            .join(jobPost.company, company).fetchJoin()
            .where(where)
            .orderBy(sortOrder(filter.sortBy))

        if (pageable.isPaged) {
            query.offset(pageable.offset).limit(pageable.pageSize.toLong())
        }

        val content = query.fetch()

        val total = queryFactory
            .select(jobPost.count())
            .from(jobPost)
            .where(where)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    fun searchAll(
        filter: JobSearchFilter,
        envExcludes: EnvExcludes,
        educExcludes: List<String>?,
        careerMonths: Int?
    ): List<JobPost> =
        queryFactory
            .selectFrom(jobPost)
            .join(jobPost.company, company).fetchJoin()
            .where(buildConditions(filter, envExcludes, educExcludes, careerMonths))
            .orderBy(sortOrder(filter.sortBy))
            .fetch()

    private fun buildConditions(
        filter: JobSearchFilter,
        envExcludes: EnvExcludes,
        educExcludes: List<String>?,
        careerMonths: Int?
    ): BooleanBuilder {
        val builder = BooleanBuilder()

        envNotIn(jobPost.envBothHands, envExcludes.bothHands)?.let { builder.and(it) }
        envNotIn(jobPost.envEyeSight, envExcludes.eyeSight)?.let { builder.and(it) }
        envNotIn(jobPost.envHandWork, envExcludes.handWork)?.let { builder.and(it) }
        envNotIn(jobPost.envLiftPower, envExcludes.liftPower)?.let { builder.and(it) }
        envNotIn(jobPost.envLstnTalk, envExcludes.lstnTalk)?.let { builder.and(it) }
        envNotIn(jobPost.envStndWalk, envExcludes.stndWalk)?.let { builder.and(it) }
        educNotIn(educExcludes)?.let { builder.and(it) }
        empTypeIn(filter.empTypes)?.let { builder.and(it) }
        salaryTypeIn(filter.salaryTypes)?.let { builder.and(it) }
        salaryBetween(filter.minSalary, filter.maxSalary)?.let { builder.and(it) }
        regionStartsWith(filter.region)?.let { builder.and(it) }
        keywordContains(filter.keyword)?.let { builder.and(it) }
        builder.and(notExpired())

        return builder
    }

    private fun envNotIn(path: StringPath, excludes: List<String>?) =
        excludes?.takeIf { it.isNotEmpty() }?.let { path.notIn(it) }

    private fun educNotIn(excludes: List<String>?) =
        excludes?.takeIf { it.isNotEmpty() }?.let { jobPost.reqEduc.notIn(it) }

    private fun empTypeIn(types: List<String>?) =
        types?.takeIf { it.isNotEmpty() }?.let { jobPost.empType.`in`(it) }

    private fun salaryTypeIn(types: List<String>?) =
        types?.takeIf { it.isNotEmpty() }?.let { jobPost.salaryType.`in`(it) }

    private fun salaryBetween(min: BigDecimal?, max: BigDecimal?) = when {
        min != null && max != null -> jobPost.salary.between(min, max)
        min != null -> jobPost.salary.goe(min)
        max != null -> jobPost.salary.loe(max)
        else -> null
    }

    private fun regionStartsWith(region: String?) = region?.let { r ->
        val variants = Region.normalize(r)
        val builder = BooleanBuilder()
        variants.forEach { builder.or(jobPost.jobLocation.startsWith(it)) }
        builder
    }

    private fun keywordContains(keyword: String?) =
        keyword?.takeIf { it.isNotBlank() }?.let { jobPost.jobNm.containsIgnoreCase(it) }

    private fun notExpired(): com.querydsl.core.types.dsl.BooleanExpression {
        val today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).toLong()
        return jobPost.offerEndDt.goe(today)
    }

    private fun sortOrder(sortBy: SortType): OrderSpecifier<*> = when (sortBy) {
        SortType.RECENT -> jobPost.regDt.desc()
        SortType.SALARY_HIGH -> jobPost.salary.desc()
        SortType.MATCH_SCORE -> jobPost.regDt.desc()
    }
}

data class EnvExcludes(
    val bothHands: List<String>? = null,
    val eyeSight: List<String>? = null,
    val handWork: List<String>? = null,
    val liftPower: List<String>? = null,
    val lstnTalk: List<String>? = null,
    val stndWalk: List<String>? = null
)
