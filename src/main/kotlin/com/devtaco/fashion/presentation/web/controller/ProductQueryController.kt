package com.devtaco.fashion.presentation.web.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.devtaco.fashion.application.services.ProductQueryUseCase
import com.devtaco.fashion.global.common.response.ApiResponse
import com.devtaco.fashion.application.model.result.MinMaxProductInCategory
import com.devtaco.fashion.application.model.result.LowestCoordiSet
import com.devtaco.fashion.application.model.result.LowestBrandCoordiSet
import com.devtaco.fashion.domain.model.Brand
import com.devtaco.fashion.domain.model.Category
import com.devtaco.fashion.domain.model.Product
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag

/**
 * 상품 조회 API 컨트롤러
 * 
 * 이 컨트롤러는 상품 조회와 관련된 REST API를 제공합니다.
 * - 상태를 변경하지 않는 읽기 전용 작업만 수행 (Query Side)
 * - 기능 1, 2, 3번 API를 구현
 * - RESTful API 설계 원칙을 따름
 * 
 * CQRS 패턴에서 Query Side를 담당합니다.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "상품 조회 API", description = "상품 조회와 관련된 REST API를 제공합니다. 상태를 변경하지 않는 읽기 전용 작업만 수행합니다.")
class ProductQueryController(
    private val productQueryUseCase: ProductQueryUseCase
) { 
    @Operation(
        summary = "카테고리별 최저가 코디 세트 조회",
        description = """
            기능 1번 API: 카테고리별 최저가 상품 조합으로 최저가 코디 세트를 조회합니다.
        
            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": {
                "coordiSet": [
                  {"category": "TOP", "brand": "A", "price": 10000},
                  {"category": "OUTER", "brand": "D", "price": 5100}
                ],
                "totalPrice": 15100
              }
            }
            ```
            
            **비즈니스 로직:**
            - 각 카테고리별로 최저가 상품을 조회
            - 모든 카테고리가 포함되어야 함
            - 총 가격을 계산하여 반환
            """
    )
    @GetMapping("/coordi-set/min-price/category")
    fun getMinPriceCoordiSetByCategory(): ApiResponse<LowestCoordiSet> {
        return productQueryUseCase.getMinPriceCoordiSetByCategory()
    }

    @Operation(
        summary = "단일 브랜드 최저가 코디 세트 조회",
        description = """
            기능 2번 API: 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가 브랜드를 조회합니다.

            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": {
                "brand": "A",
                "category": [
                  {"category": "TOP", "price": 11200},
                  {"category": "OUTER", "price": 5500}
                ],
                "totalPrice": 16700
              }
            }
            ```
            
            **비즈니스 로직:**
            - 브랜드별, 카테고리별 최저가 상품을 조회
            - 모든 카테고리를 포함하는 브랜드들을 필터링
            - 최저가 브랜드 선정 (동일 가격 시 브랜드 ID로 결정)
            """
    )
    @GetMapping("/coordi-set/min-price/brand")
    fun getLowestPriceCoordiSetByBrand(): ApiResponse<LowestBrandCoordiSet> {
        return productQueryUseCase.getLowestPriceCoordiSetByBrand()
    }

    @Operation(
        summary = "카테고리별 최저가/최고가 조회",
        description = """
            기능 3번 API: 특정 카테고리에서 최저가/최고가 브랜드와 상품 가격을 조회합니다.

            
            **파라미터:**
            - category: 조회할 카테고리명 (예: "top", "outer", "pants")
            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": {
                "category": "TOP",
                "minProduct": {
                  "brand": "C",
                  "price": 10000
                },
                "maxProduct": {
                  "brand": "I",
                  "price": 11400
                }
              }
            }
            ```
            
            **비즈니스 로직:**
            - 특정 카테고리의 모든 상품을 조회
            - 최저가/최고가 상품을 찾아서 반환
            - 브랜드 정보와 함께 가격 정보 제공
            """
    )
    @GetMapping("/price-range")
    fun getMinMaxPriceByCategory(
        @Parameter(description = "조회할 카테고리명 (예: top, outer, pants)", example = "top")
        @RequestParam category: String
    ): ApiResponse<MinMaxProductInCategory> {
        return productQueryUseCase.getMinMaxPriceByCategory(category)
    }

    @GetMapping()
    fun getAllProducts(): ApiResponse<List<Product>> {
        return productQueryUseCase.getAllProducts()
    }
    
    @Operation(
        summary = "전체 브랜드 목록 조회",
        description = """
            전체 브랜드 목록을 ID 순으로 정렬하여 조회합니다.
            
            **사용법:**
            - 등록된 모든 브랜드 정보를 조회
            - 브랜드 ID 순으로 정렬되어 반환
            - 브랜드 생성 시 참고용으로 사용
            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": [
                {"id": 1, "name": "A"},
                {"id": 2, "name": "B"},
                {"id": 3, "name": "C"}
              ]
            }
            ```
            """
    )
    @GetMapping("/brands")
    fun getBrandList(): ApiResponse<List<Brand>> {
        return productQueryUseCase.getBrandList()
    }

    @Operation(
        summary = "전체 카테고리 목록 조회",
        description = """
            전체 카테고리 목록을 ID 순으로 정렬하여 조회합니다.
            
            **사용법:**
            - 등록된 모든 카테고리 정보를 조회
            - 카테고리 ID 순으로 정렬되어 반환
            - 상품 생성 시 참고용으로 사용
            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": [
                {"id": 1, "name": "TOP"},
                {"id": 2, "name": "OUTER"},
                {"id": 3, "name": "PANTS"}
              ]
            }
            ```
            """
    )
    @GetMapping("/categories")
    fun getCategoryList(): ApiResponse<List<Category>> {
        return productQueryUseCase.getCategoryList()
    }
}