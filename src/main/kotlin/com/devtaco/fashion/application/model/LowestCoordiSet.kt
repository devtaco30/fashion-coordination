package com.devtaco.fashion.application.model.result

import com.devtaco.fashion.domain.model.Product

/**
 * 카테고리별 최저가 상품 조합 결과
 * 
 * 기능 1번 API 응답을 위한 데이터 클래스입니다.
 * 각 카테고리별로 최저가 상품을 조합한 결과와 총 가격을 포함합니다.
 */
data class LowestCoordiSet(
    val coordiSet: List<ProductInfo>,
    val totalPrice: Int
)

/**
 * 상품 정보 응답 데이터
 * 
 * 카테고리별 최저가 상품의 정보를 담는 클래스입니다.
 * 카테고리명, 브랜드명, 가격 정보를 포함합니다.
 */
open class ProductInfo(
    val category: String,
    val brand: String,
    val price: Int,
) {
    companion object {
        fun from(product: Product): ProductInfo {
            return ProductInfo(
                category = product.category.name,
                brand = product.brand.name,
                price = product.price
            )
        }
    }
}
