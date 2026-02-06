package me.bfapplicant.domain.enums

interface EnvCondition {
    val label: String
    val level: Int
}

enum class EnvBothHands(override val label: String, override val level: Int) : EnvCondition {
    NO_INFO("정보없음", 0),
    ONE_HAND("한손작업 가능", 1),
    ONE_HAND_ASSIST("한손보조작업 가능", 2),
    BOTH_HANDS("양손작업 가능", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

enum class EnvEyeSight(override val label: String, override val level: Int) : EnvCondition {
    NO_INFO("정보없음", 0),
    LARGE_PRINT("비교적 큰 인쇄물을 읽을 수 있음", 1),
    DAILY_ACTIVITY("일상적 활동 가능", 2),
    FINE_PRINT("아주 작은 글씨를 읽을 수 있음", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

enum class EnvHandWork(override val label: String, override val level: Int) : EnvCondition {
    NO_INFO("정보없음", 0),
    LARGE_ASSEMBLY("큰 물품 조립가능", 1),
    SMALL_ASSEMBLY("작은 물품 조립가능", 2),
    PRECISION("정밀한 작업가능", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

enum class EnvLiftPower(override val label: String, override val level: Int) : EnvCondition {
    NO_INFO("정보없음", 0),
    UNDER_5KG("5Kg 이내의 물건을 다룰 수 있음", 1),
    UNDER_20KG("5~20Kg의 물건을 다룰 수 있음", 2),
    OVER_20KG("20Kg 이상의 물건을 다룰 수 있음", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

enum class EnvLstnTalk(override val label: String, override val level: Int) : EnvCondition {
    NO_INFO("정보없음", 0),
    DIFFICULT("듣고 말하는 작업 어려움", 1),
    SIMPLE("간단한 듣고 말하기 가능", 2),
    FLUENT("듣고 말하기에 어려움 없음", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}

enum class EnvStndWalk(override val label: String, override val level: Int) : EnvCondition {
    NO_INFO("정보없음", 0),
    DIFFICULT("서거나 걷는 일 어려움", 1),
    PARTIAL("일부 서서하는 작업 가능", 2),
    PROLONGED("오랫동안 가능", 3);

    companion object {
        private val map = entries.associateBy { it.label }
        fun fromLabel(label: String) = map[label] ?: NO_INFO
    }
}
