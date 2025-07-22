package com.devtaco.fashion.application.model.result

import com.devtaco.fashion.domain.model.Product

/**
 * 카테고리별 최저가/최고가 상품 정보
 * 
 * 기능 3번 API 응답을 위한 데이터 클래스입니다.
 * 특정 카테고리에서 최저가와 최고가 상품의 브랜드 정보를 포함합니다.
 */
data class MinMaxProductInCategory(
    val category: String,
    val minPrice: List<BrandPriceInfo>,
    val maxPrice: List<BrandPriceInfo>
) {
    companion object {
        fun from(category: String, minProduct: Product, maxProduct: Product): MinMaxProductInCategory {
            return MinMaxProductInCategory(
                category = category,
                minPrice = listOf(BrandPriceInfo.from(minProduct)),
                maxPrice = listOf(BrandPriceInfo.from(maxProduct))
            )
        }
    }
}

/**
 * 브랜드 가격 정보 응답 데이터
 * 
 * 카테고리별 최저가/최고가 상품의 브랜드 정보를 담는 클래스입니다.
 * 브랜드명과 가격 정보를 포함합니다.
 */
open class BrandPriceInfo(
    val brand: String,
    val price: Int,
) {
    companion object {
        fun from(product: Product): BrandPriceInfo {
            return BrandPriceInfo(
                brand = product.brand.name,
                price = product.price
            )
        }
    }
}


