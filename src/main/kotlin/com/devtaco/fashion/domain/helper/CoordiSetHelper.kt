package com.devtaco.fashion.domain.helper

import com.devtaco.fashion.application.model.result.LowestBrandCoordiSet
import com.devtaco.fashion.application.model.result.PriceInfo
import com.devtaco.fashion.application.model.result.ProductInfo
import com.devtaco.fashion.application.model.result.LowestCoordiSet
import com.devtaco.fashion.domain.model.*
import com.devtaco.fashion.global.common.exceptions.InsufficientDataException

/**
 * 코디세트 관련 도메인 헬퍼 객체
 * 
 * 이 객체는 코디세트 생성과 관련된 복잡한 비즈니스 로직을 캡슐화합니다.
 * - 카테고리별 최저가 상품 조합 생성
 * - 브랜드별 최저가 코디세트 생성
 * - 카테고리 완성도 검증
 * 
 * 도메인 서비스와 달리 상태를 가지지 않는 순수 함수들의 집합입니다.
 */
object CoordiSetHelper {
    
    private const val NO_COORDI_SET_MESSAGE = "모든 카테고리를 포함하는 코디세트를 구성할 수 없습니다. 일부 카테고리에 상품이 없을 수 있습니다."

    /**
     * 카테고리별 최저가 상품 목록을 받아 코디세트를 생성하여 반환
     * 
     * 이 메서드는 이미 카테고리별 최저가 상품이 선별된 목록을 입력받아
     * 요구되는 카테고리 수를 충족하는지 검증하고, 
     * 코디세트 형태로 변환하는 역할을 합니다.
     *
     */
    fun createLowestPriceCoordiSet(
        products: List<Product>, 
        requiredCategoryCount: Long
    ): LowestCoordiSet {
        
        if (!hasAllRequiredCategories(products, requiredCategoryCount)) {
            throw InsufficientDataException(NO_COORDI_SET_MESSAGE)
        }

        val coordiSet = products.map { ProductInfo.from(it) }
        val totalPrice = products.sumOf { it.price }

        return LowestCoordiSet(
            coordiSet = coordiSet,
            totalPrice = totalPrice
        )
    }
    
    /**
     * 브랜드별, 카테고리별 최저가 상품 목록에서 
     * 모든 카테고리를 포함하는 브랜드 중 최저가 브랜드의 코디세트를 생성하여 반환
     * 
     * 이 메서드는 기능 2번 API를 위한 핵심 비즈니스 로직을 구현합니다:
     * 1. 모든 카테고리를 포함하는 브랜드들을 필터링
     * 2. 브랜드별 총 가격을 계산하여 최저가 브랜드 선정
     * 3. 동일한 총 가격일 때는 브랜드 ID가 작은 브랜드 선택 (예측 가능한 선택 기준)
     * 
     */
    fun createLowestPriceBrandCoordiSet(
        products: List<Product>, 
        requiredCategoryCount: Long
    ): LowestBrandCoordiSet {
        val filteredProducts = filterLowestPriceBrandProducts(products, requiredCategoryCount)

        if (!hasAllRequiredCategories(filteredProducts, requiredCategoryCount)) {
            throw InsufficientDataException(NO_COORDI_SET_MESSAGE)
        }

        val priceInfos = filteredProducts.map { PriceInfo.from(it) }
        val brand = filteredProducts.first().brand.name
        val totalPrice = filteredProducts.sumOf { it.price }
        
        return LowestBrandCoordiSet(
            brand = brand,
            category = priceInfos,
            totalPrice = totalPrice
        )
    }   
    
    /**
     * 상품 목록이 모든 필요한 카테고리를 포함하는지 검증하는 메서드
     * 
     * @param products 검증할 상품 목록
     * @param requiredCategoryCount 필수 카테고리 수
     * @return 모든 카테고리를 포함하면 true, 아니면 false
     */
    private fun hasAllRequiredCategories(products: List<Product>, requiredCategoryCount: Long): Boolean {
        return products.map { it.category.id }.distinct().size == requiredCategoryCount.toInt()
    }
    
    /**
     * 브랜드별, 카테고리별 최저가 상품 목록에서 
     * 모든 카테고리를 포함하는 브랜드 중 최저가 브랜드의 상품들을 필터링하여 반환
     * 
     * 이 메서드는 복잡한 비즈니스 로직을 단계별로 분리하여 구현합니다:
     * 1. 브랜드별로 상품들을 그룹화
     * 2. 모든 카테고리를 포함하는 브랜드들만 필터링
     * 3. 최저가 브랜드 선정 (동일한 가격일 때는 브랜드 ID로 결정)
     * 
     * @param products 브랜드별, 카테고리별 최저가 상품 목록
     * @param requiredCategoryCount 필수 카테고리 수
     * @return 최저가 브랜드의 상품 목록
     * @throws InsufficientDataException 모든 카테고리를 포함하는 브랜드가 없을 때
     */
    private fun filterLowestPriceBrandProducts(
        products: List<Product>, 
        requiredCategoryCount: Long
    ): List<Product> {
        // 1. 브랜드별로 상품들을 그룹화
        val groupedByBrand = products.groupBy { it.brand.id }
        
        // 2. 모든 카테고리를 포함하는 브랜드들만 필터링
        val brandsWithAllCategories = groupedByBrand.filter { (_, products) ->
            hasAllRequiredCategories(products, requiredCategoryCount)
        }
        
        // 3. 모든 카테고리를 포함하는 브랜드가 없으면 예외 발생
        if (brandsWithAllCategories.isEmpty()) {
            throw InsufficientDataException(NO_COORDI_SET_MESSAGE)
        }
        
        // 4. 최저가 브랜드 선정 (동일한 가격일 때는 브랜드 ID가 작은 브랜드 선택)
        val lowestPriceBrandId = findLowestPriceBrandId(brandsWithAllCategories)
        
        // 5. 최저가 브랜드의 상품 목록 반환
        return brandsWithAllCategories[lowestPriceBrandId]!!
    }
    
    /**
     * 브랜드별 총 가격을 비교하여 최저가 브랜드 ID를 찾는 메서드
     * 
     * Linear Search 알고리즘을 사용하여 O(n) 시간복잡도로 최소값을 찾습니다.
     * 동일한 총 가격일 때는 브랜드 ID가 작은 브랜드를 선택하여 예측 가능한 결과를 보장합니다.
     * 
     * @param brandsWithAllCategories 모든 카테고리를 포함하는 브랜드들의 상품 목록
     * @return 최저가 브랜드의 ID
     */
    private fun findLowestPriceBrandId(brandsWithAllCategories: Map<Long, List<Product>>): Long {
        return brandsWithAllCategories
            .map { (brandId, products) -> 
                brandId to products.sumOf { it.price }
            }
            .reduce { (brandId1, totalPrice1), (brandId2, totalPrice2) ->
                when {
                    totalPrice1 < totalPrice2 -> brandId1 to totalPrice1
                    totalPrice1 > totalPrice2 -> brandId2 to totalPrice2
                    else -> {
                        if (brandId1 < brandId2) {
                            brandId1 to totalPrice1
                        } else {
                            brandId2 to totalPrice2
                        }
                    }
                }
            }
            .first
    }
} 