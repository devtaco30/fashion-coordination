package com.devtaco.fashion.application.model.result

import com.devtaco.fashion.domain.model.Product

/**
 * 단일 브랜드 최저가 상품 조합 결과
 * 
 * 기능 2번 API 응답을 위한 데이터 클래스입니다.
 * 한 브랜드의 모든 카테고리 상품을 조합한 결과와 총 가격을 포함합니다.
 */
data class LowestBrandCoordiSet(
    val brand: String,
    val category: List<PriceInfo>,
    val totalPrice: Int
)

/**
 * 가격 정보 응답 데이터
 * 
 * 브랜드별 최저가 상품의 가격 정보를 담는 클래스입니다.
 * 카테고리명과 가격 정보를 포함합니다.
 */
open class PriceInfo(
    val category: String,
    val price: Int,
) {
    companion object {
        fun from(product: Product): PriceInfo {
            return PriceInfo(
                category = product.category.name,
                price = product.price
            )
        }
    }
}
