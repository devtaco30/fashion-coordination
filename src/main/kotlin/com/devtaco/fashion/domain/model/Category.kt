package com.devtaco.fashion.domain.model

/**
 * 카테고리(Category) 도메인 객체
 * - 카테고리명만 보유, 불변
 */
data class Category(
    val id: Long = 0L,
    val name: String
) {
    init {
        require(name.isNotBlank()) { "카테고리명은 비어있을 수 없습니다" }
        require(name.length <= 50) { "카테고리명은 50자를 초과할 수 없습니다. 현재 길이: ${name.length}" }
    }

    companion object {
        // 변환 로직은 Infrastructure Layer의 Mapper에서 담당
    }   
}