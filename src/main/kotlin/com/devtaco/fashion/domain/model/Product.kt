package com.devtaco.fashion.domain.model

/**
 * 의류(Cloth) 도메인 객체
 * - 카테고리, 브랜드, 가격, 상태값을 가짐
 * - 식별자, 불변성, 타입 안전성 보장
 *
 * [id 생성 관련]
 * - 신규 생성 시 id는 0으로 두고, DB 저장 후 할당된 값을 사용함
 * - 도메인 객체에서 id를 직접 생성하지 않음
 */
data class Product(
    val id: Long = 0L,
    val category: Category,
    val brand: Brand,
    var price: Int,
    val status: ProductStatus
) {
    init {
        require(price > 0) { "상품 가격은 0보다 커야 합니다. 현재 가격: $price" }
        require(category.name.isNotBlank()) { "카테고리명은 비어있을 수 없습니다" }
        require(brand.name.isNotBlank()) { "브랜드명은 비어있을 수 없습니다" }
    }

    companion object {
        // 변환 로직은 Infrastructure Layer의 Mapper에서 담당
    }
}

/**
 * 의류 상태값
 */
enum class ProductStatus {
    NORMAL, SOLD_OUT, DELETED
} 