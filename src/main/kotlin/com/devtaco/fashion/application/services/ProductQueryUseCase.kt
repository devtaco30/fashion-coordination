package com.devtaco.fashion.application.services

import com.devtaco.fashion.application.model.result.LowestCoordiSet
import com.devtaco.fashion.application.model.result.LowestBrandCoordiSet
import com.devtaco.fashion.application.model.result.MinMaxProductInCategory
import com.devtaco.fashion.application.model.result.BrandPriceInfo
import com.devtaco.fashion.domain.model.*
import com.devtaco.fashion.domain.helper.CoordiSetHelper.createLowestPriceBrandCoordiSet
import com.devtaco.fashion.domain.helper.CoordiSetHelper.createLowestPriceCoordiSet
import com.devtaco.fashion.domain.service.ProductService
import com.devtaco.fashion.domain.service.CategoryService
import com.devtaco.fashion.domain.service.BrandService
import com.devtaco.fashion.global.common.response.ApiResponse
import com.devtaco.fashion.global.common.exceptions.InsufficientDataException
import org.springframework.stereotype.Service

/**
 * 상품 조회 유스케이스 (Query Side)
 * 
 * 이 클래스는 CQRS 패턴의 Query Side를 구현합니다.
 * - 상태를 변경하지 않는 읽기 전용 작업만 수행
 * - 복잡한 조회 로직을 조합하여 비즈니스 요구사항을 충족
 * - 도메인 서비스와 도메인 헬퍼를 조합하여 결과를 생성
 * 
 * 기능 1, 2, 3번 API의 비즈니스 로직을 구현합니다.
 */
@Service
class ProductQueryUseCase(
    private val productService: ProductService,
    private val categoryService: CategoryService,
    private val brandService: BrandService
) {
    
    /**
     * 카테고리별 최저가 상품과 총액을 조회
     * 
     * 기능 1번 API를 위한 유스케이스입니다.
     * - 각 카테고리별로 최저가 상품을 조회
     * - 총 구매 비용을 계산
     * - 사용자가 카테고리별로 다른 브랜드의 상품을 조합하여 최저가로 구매할 수 있는 정보 제공
     */
    fun getMinPriceCoordiSetByCategory(): ApiResponse<LowestCoordiSet> {
        val minPriceProducts = productService.findMinPriceCoordiSetEachCategory()
        val requiredCategoryCount = categoryService.count()
        val coordiSetResult = createLowestPriceCoordiSet(minPriceProducts, requiredCategoryCount)
        return ApiResponse.success(coordiSetResult)
    }

    /**
     * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가 브랜드와 총액을 조회
     * 
     * 기능 2번 API를 위한 유스케이스입니다.
     * - 브랜드별, 카테고리별 최저가 상품을 조회
     * - 모든 카테고리를 포함하는 브랜드들을 필터링하여 최저가 브랜드 선정
     * - 동일한 총 가격일 때는 브랜드 ID가 작은 브랜드 선택
     * - 사용자가 한 브랜드의 상품만으로 코디를 완성할 때의 최저가 정보 제공
     */
    fun getLowestPriceCoordiSetByBrand(): ApiResponse<LowestBrandCoordiSet> {
        val requiredCategoryCount = categoryService.count()
        val rawResult = productService.findMinPriceBrandCoordiSet()
        val coordiSetResult = createLowestPriceBrandCoordiSet(rawResult, requiredCategoryCount)
        return ApiResponse.success(coordiSetResult)
    }

    /**
     * 특정 카테고리에서 최저가/최고가 브랜드와 상품 가격을 조회
     * 
     * 기능 3번 API를 위한 유스케이스입니다.
     * - 특정 카테고리의 최저가/최고가 상품을 조회
     * - 브랜드 정보와 함께 반환
     * - 사용자가 특정 카테고리의 가격 범위를 파악할 수 있는 정보 제공
     */
    fun getMinMaxPriceByCategory(categoryName: String): ApiResponse<MinMaxProductInCategory> {
        val category = categoryService.findByName(categoryName.uppercase())
        val (minProduct, maxProduct) = productService.findMinMaxPriceProductInCategory(category.id)
        val result = MinMaxProductInCategory.from(category.name, minProduct, maxProduct)
        return ApiResponse.success(result)
    }

    fun getBrandList(): ApiResponse<List<Brand>> {
        val brandList = brandService.findAll()
        return ApiResponse.success(brandList)
    }

    fun getCategoryList(): ApiResponse<List<Category>> {
        val categoryList = categoryService.findAll()
        return ApiResponse.success(categoryList)
    }

    fun getAllProducts(): ApiResponse<List<Product>> {
        val productList = productService.findAll()
        return ApiResponse.success(productList)
    }
} 