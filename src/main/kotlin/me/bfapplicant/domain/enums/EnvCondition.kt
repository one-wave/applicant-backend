package me.bfapplicant.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

interface EnvCondition {
    val label: String
    val level: Int
}

@Schema(
    enumAsRef = true,
    description = """
양손작업 능력:
* IMPOSSIBLE - 불가능
* NO_INFO - 정보없음
* ONE_HAND - 한손작업 가능
* ONE_HAND_ASSIST - 한손보조작업 가능
* BOTH_HANDS - 양손작업 가능"""
)
enum class EnvBothHands(override val label: String, override val level: Int) : EnvCondition {
    IMPOSSIBLE("불가능", 0),
    NO_INFO("정보없음", 0),
    ONE_HAND("한손작업 가능", 1),
    ONE_HAND_ASSIST("한손보조작업 가능", 2),
    BOTH_HANDS("양손작업 가능", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

@Schema(
    enumAsRef = true,
    description = """
시력 수준:
* IMPOSSIBLE - 불가능
* NO_INFO - 정보없음
* LARGE_PRINT - 비교적 큰 인쇄물을 읽을 수 있음
* DAILY_ACTIVITY - 일상적 활동 가능
* FINE_PRINT - 아주 작은 글씨를 읽을 수 있음"""
)
enum class EnvEyeSight(override val label: String, override val level: Int) : EnvCondition {
    IMPOSSIBLE("불가능", 0),
    NO_INFO("정보없음", 0),
    LARGE_PRINT("비교적 큰 인쇄물을 읽을 수 있음", 1),
    DAILY_ACTIVITY("일상적 활동 가능", 2),
    FINE_PRINT("아주 작은 글씨를 읽을 수 있음", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

@Schema(
    enumAsRef = true,
    description = """
손작업 정밀도:
* IMPOSSIBLE - 불가능
* NO_INFO - 정보없음
* LARGE_ASSEMBLY - 큰 물품 조립가능
* SMALL_ASSEMBLY - 작은 물품 조립가능
* PRECISION - 정밀한 작업가능"""
)
enum class EnvHandWork(override val label: String, override val level: Int) : EnvCondition {
    IMPOSSIBLE("불가능", 0),
    NO_INFO("정보없음", 0),
    LARGE_ASSEMBLY("큰 물품 조립가능", 1),
    SMALL_ASSEMBLY("작은 물품 조립가능", 2),
    PRECISION("정밀한 작업가능", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

@Schema(
    enumAsRef = true,
    description = """
들기 힘:
* IMPOSSIBLE - 불가능
* NO_INFO - 정보없음
* UNDER_5KG - 5Kg 이내의 물건을 다룰 수 있음
* UNDER_20KG - 5~20Kg의 물건을 다룰 수 있음
* OVER_20KG - 20Kg 이상의 물건을 다룰 수 있음"""
)
enum class EnvLiftPower(override val label: String, override val level: Int) : EnvCondition {
    IMPOSSIBLE("불가능", 0),
    NO_INFO("정보없음", 0),
    UNDER_5KG("5Kg 이내의 물건을 다룰 수 있음", 1),
    UNDER_20KG("5~20Kg의 물건을 다룰 수 있음", 2),
    OVER_20KG("20Kg 이상의 물건을 다룰 수 있음", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

@Schema(
    enumAsRef = true,
    description = """
듣고 말하기 능력:
* IMPOSSIBLE - 불가능
* NO_INFO - 정보없음
* DIFFICULT - 듣고 말하는 작업 어려움
* SIMPLE - 간단한 듣고 말하기 가능
* FLUENT - 듣고 말하기에 어려움 없음"""
)
enum class EnvLstnTalk(override val label: String, override val level: Int) : EnvCondition {
    IMPOSSIBLE("불가능", 0),
    NO_INFO("정보없음", 0),
    DIFFICULT("듣고 말하는 작업 어려움", 1),
    SIMPLE("간단한 듣고 말하기 가능", 2),
    FLUENT("듣고 말하기에 어려움 없음", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

@Schema(
    enumAsRef = true,
    description = """
서서 걷기 능력:
* IMPOSSIBLE - 불가능
* NO_INFO - 정보없음
* DIFFICULT - 서거나 걷는 일 어려움
* PARTIAL - 일부 서서하는 작업 가능
* PROLONGED - 오랫동안 가능"""
)
enum class EnvStndWalk(override val label: String, override val level: Int) : EnvCondition {
    IMPOSSIBLE("불가능", 0),
    NO_INFO("정보없음", 0),
    DIFFICULT("서거나 걷는 일 어려움", 1),
    PARTIAL("일부 서서하는 작업 가능", 2),
    PROLONGED("오랫동안 가능", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}
