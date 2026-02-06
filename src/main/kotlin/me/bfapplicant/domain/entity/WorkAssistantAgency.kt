package me.bfapplicant.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "work_assistant_agency")
class WorkAssistantAgency(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val branch: String,

    @Column(name = "agency_name", nullable = false)
    val agencyName: String,

    @Column(name = "zip_code")
    val zipCode: String?,

    @Column(nullable = false)
    val address: String,

    @Column(name = "biz_no")
    val bizNo: String?,

    val phone: String?,

    @Column(nullable = false)
    val region: String
)
