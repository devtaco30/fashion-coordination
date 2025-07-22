package com.devtaco.fashion.domain.model

/**
 * 브랜드(Brand) 값 객체
 * - 브랜드명만 보유, 불변
 */
data class Brand(
    val id: Long = 0L,
    val name: String,
) {
    init {
        require(name.isNotBlank()) { "브랜드명은 비어있을 수 없습니다" }
        require(name.length <= 100) { "브랜드명은 100자를 초과할 수 없습니다. 현재 길이: ${name.length}" }
    }

    companion object {
        // 변환 로직은 Infrastructure Layer의 Mapper에서 담당
    }
}