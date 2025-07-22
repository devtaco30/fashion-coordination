package com.devtaco.fashion.presentation.web.controller

import com.devtaco.fashion.application.dto.CreateProductRequest
import com.devtaco.fashion.application.dto.UpdateProductRequest
import com.devtaco.fashion.application.dto.CreateBrandRequest
import com.devtaco.fashion.application.dto.UpdateBrandRequest
import jakarta.validation.constraints.Positive
import com.devtaco.fashion.application.services.ProductManagementUseCase
import com.devtaco.fashion.domain.model.Brand
import com.devtaco.fashion.domain.model.Product
import com.devtaco.fashion.global.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag

/**
 * 상품 관리 API 컨트롤러
 * 
 * 이 컨트롤러는 상품과 브랜드 관리와 관련된 REST API를 제공합니다.
 * - 상태를 변경하는 작업만 수행 (Command Side)
 * - 기능 4번 API를 구현 (브랜드 및 상품 CRUD)
 * - RESTful API 설계 원칙을 따름
 * - 관리자 기능으로 분류
 * 
 * - 단, 현재구조에서 '삭제' 는 soft delete 가  아닌 hard delete 로 구현되어 있습니다.
 * 
 * CQRS 패턴에서 Command Side를 담당합니다.
 */
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "상품 관리 API", description = "상품과 브랜드 관리와 관련된 REST API를 제공합니다. 상태를 변경하는 작업만 수행합니다.")
class ProductManagementController(
    private val productManagementUseCase: ProductManagementUseCase
) {
    
    @Operation(
        summary = "브랜드 생성",
        description = """
            새로운 브랜드를 생성합니다.
            
            **사용법:**
            - 브랜드명을 입력하여 새로운 브랜드 생성
            - 중복된 브랜드명은 허용하지 않음
            - 생성된 브랜드는 상품 생성 시 사용 가능
            
            **사전 준비:**
            - 기존 브랜드 목록 확인: `GET /api/v1/products/brands`
            - 중복 브랜드명 확인 후 생성
            
            **요청 예시:**
            ```json
            {
              "name": "새로운 브랜드"
            }
            ```
            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": {
                "id": 10,
                "name": "새로운 브랜드"
              },
              "message": "브랜드가 성공적으로 생성되었습니다"
            }
            ```
            
            **비즈니스 규칙:**
            - 브랜드명은 필수 입력값
            - 동일한 브랜드명이 이미 존재하면 에러 발생
            - 생성 후 브랜드 ID가 자동 할당됨
            """
    )
    @PostMapping("/brands")
    fun createBrand(@Valid @RequestBody request: CreateBrandRequest): ApiResponse<Brand> {
        return productManagementUseCase.createBrand(request)
    }

    @PutMapping("/brands/{id}")
    fun updateBrand(
        @PathVariable @Positive(message = "브랜드 ID는 0보다 커야 합니다") id: Long, 
        @Valid @RequestBody request: UpdateBrandRequest
    ): ApiResponse<Brand> {
        return productManagementUseCase.updateBrand(id, request)
    }

    @DeleteMapping("/brands/{id}")
    fun deleteBrand(@PathVariable @Positive(message = "브랜드 ID는 0보다 커야 합니다") id: Long): ApiResponse<Unit> {
        return productManagementUseCase.deleteBrand(id)
    }

    @Operation(
        summary = "상품 생성",
        description = """
            새로운 상품을 생성합니다.
            
            **사용법:**
            - 브랜드 ID, 카테고리 ID, 가격을 입력하여 상품 생성
            - 브랜드와 카테고리는 미리 생성되어 있어야 함
            - 가격은 0보다 커야 함
            
            **사전 준비:**
            - 브랜드 목록 확인: `GET /api/v1/products/brands`
            - 카테고리 목록 확인: `GET /api/v1/products/categories`
            - 존재하는 브랜드 ID와 카테고리 ID 확인 후 생성
            
            **요청 예시:**
            ```json
            {
              "brandId": 1,
              "categoryId": 1,
              "price": 15000
            }
            ```
            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": {
                "id": 100,
                "category": {"id": 1, "name": "TOP"},
                "brand": {"id": 1, "name": "A"},
                "price": 15000,
                "status": "NORMAL"
              },
              "message": "상품이 성공적으로 생성되었습니다"
            }
            ```
            
            **비즈니스 규칙:**
            - 브랜드 ID와 카테고리 ID는 필수 입력값
            - 가격은 0보다 커야 함
            - 존재하지 않는 브랜드/카테고리 ID는 에러 발생
            - 생성 후 상품 ID가 자동 할당됨
            """
    )
    @PostMapping("/products")
    fun createProduct(@Valid @RequestBody request: CreateProductRequest): ApiResponse<Product> {
        return productManagementUseCase.createProduct(request)
    }
    
    @Operation(
        summary = "상품 수정",
        description = """
            기존 상품의 정보를 수정합니다.
            
            **사용법:**
            - 상품 ID를 path variable로 지정
            - 수정할 가격을 request body에 입력
            - 현재는 가격만 수정 가능
            
            **요청 예시:**
            ```json
            {
              "price": 20000
            }
            ```
            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": {
                "id": 1,
                "category": {"id": 1, "name": "TOP"},
                "brand": {"id": 1, "name": "A"},
                "price": 20000,
                "status": "NORMAL"
              },
              "message": "상품이 성공적으로 수정되었습니다"
            }
            ```
            
            **비즈니스 규칙:**
            - 상품 ID는 0보다 커야 함
            - 가격은 0보다 커야 함
            - 존재하지 않는 상품 ID는 에러 발생
            - 수정 후 updatedAt이 자동 업데이트됨
            """
    )
    @PutMapping("/products/{id}")
    fun updateProduct(
        @Parameter(description = "수정할 상품의 ID", example = "1")
        @PathVariable @Positive(message = "상품 ID는 0보다 커야 합니다") id: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ApiResponse<Product> {
        return productManagementUseCase.updateProduct(id, request)
    }
            
    @Operation(
        summary = "상품 삭제",
        description = """
            상품을 삭제합니다.
            
            **사용법:**
            - 상품 ID를 path variable로 지정
            - 해당 상품을 데이터베이스에서 완전 삭제 (Hard Delete)
            - 삭제 후 복구 불가능
            
            **응답 예시:**
            ```json
            {
              "success": true,
              "data": null,
              "message": "상품이 성공적으로 삭제되었습니다"
            }
            ```
            
            **비즈니스 규칙:**
            - 상품 ID는 0보다 커야 함
            - 존재하지 않는 상품 ID는 에러 발생
            - 삭제 후 해당 상품은 조회 불가능
            - 현재는 Soft Delete가 아닌 Hard Delete로 구현됨
            """
    )
    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @Parameter(description = "삭제할 상품의 ID", example = "1")
        @PathVariable @Positive(message = "상품 ID는 0보다 커야 합니다") id: Long
    ): ApiResponse<Unit> {
        return productManagementUseCase.deleteProduct(id)
    }
} 