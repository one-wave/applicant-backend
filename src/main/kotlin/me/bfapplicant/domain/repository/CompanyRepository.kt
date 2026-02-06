package me.bfapplicant.domain.repository

import me.bfapplicant.domain.entity.Company
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CompanyRepository : JpaRepository<Company, UUID>
