package com.devtaco.fashion.application.services

import com.devtaco.fashion.application.dto.CreateProductRequest
import com.devtaco.fashion.application.dto.UpdateProductRequest
import com.devtaco.fashion.application.dto.CreateBrandRequest
import com.devtaco.fashion.application.dto.UpdateBrandRequest
import com.devtaco.fashion.domain.model.*
import com.devtaco.fashion.domain.service.BrandService
import com.devtaco.fashion.domain.service.CategoryService
import com.devtaco.fashion.domain.service.ProductService
import com.devtaco.fashion.global.common.response.ApiResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 상품 관리 유스케이스 (Command Side)
 * 
 * 이 클래스는 CQRS 패턴의 Command Side를 구현합니다.
 * - 상태를 변경하는 작업만 수행 (Create, Update, Delete)
 * - 트랜잭션 관리
 * - 여러 도메인 서비스를 조합하여 복잡한 비즈니스 로직 구현
 * 
 * 기능 4번 API의 비즈니스 로직을 구현합니다.
 */
@Service
@Transactional
class ProductManagementUseCase(
    private val brandService: BrandService,
    private val categoryService: CategoryService,
    private val productService: ProductService
) {
    
    /**
     * 새로운 브랜드를 생성
     * 
     * 브랜드 생성 시 다음 비즈니스 규칙을 검증합니다:
     * - 브랜드명 중복 체크 (BrandService에서 처리)
     * 
     * @param request 브랜드 생성 요청
     * @return 생성된 브랜드
     */
    fun createBrand(request: CreateBrandRequest): ApiResponse<Brand> {
        val brand = Brand(name = request.name)
        val savedBrand = brandService.save(brand)
        return ApiResponse.success(savedBrand, "브랜드가 성공적으로 생성되었습니다")
    }

    /**
     * 브랜드 정보를 업데이트
     * 
     * @param id 업데이트할 브랜드 ID
     * @param request 브랜드 업데이트 요청
     * @return 업데이트된 브랜드
     */
    fun updateBrand(id: Long, request: UpdateBrandRequest): ApiResponse<Brand> {
        val existingBrand = brandService.findById(id)
        val updatedBrand = existingBrand.copy(name = request.name)
        brandService.save(updatedBrand)
        return ApiResponse.success(updatedBrand, "브랜드가 성공적으로 수정되었습니다")
    }

    /**
     * 브랜드를 삭제
     * 
     * @param id 삭제할 브랜드 ID
     * @return 삭제 성공 응답
     */
    fun deleteBrand(id: Long): ApiResponse<Unit> {
        brandService.deleteById(id)
        return ApiResponse.success(Unit, "브랜드가 성공적으로 삭제되었습니다")
    }
    
    /**
     * 새로운 상품을 생성
     * 
     * 상품 생성 시 다음 비즈니스 규칙을 검증합니다:
     * - 브랜드 존재 여부 확인
     * - 카테고리 존재 여부 확인
     * - 동일한 브랜드-카테고리 조합의 상품이 이미 존재하는지 확인
     * 
     * @param request 상품 생성 요청
     * @return 생성된 상품
     */
    fun createProduct(request: CreateProductRequest): ApiResponse<Product> {
        // 1. 브랜드 조회 (존재하지 않으면 에러)
        val brand = brandService.findById(request.brandId)
        
        // 2. 카테고리 조회 (존재하지 않으면 에러)
        val category = categoryService.findById(request.categoryId)
        
        // 3. 상품 생성
        val product = Product(
            id = 0L,
            category = category,
            brand = brand,
            price = request.price,
            status = ProductStatus.NORMAL
        )
        
        val savedProduct = productService.save(product)
        return ApiResponse.success(savedProduct, "상품이 성공적으로 생성되었습니다")
    }
    
    /**
     * 상품 가격을 업데이트
     * 
     * 상품 업데이트 시 다음 비즈니스 규칙을 검증합니다:
     * - 상품 존재 여부 확인 (ProductService에서 처리)
     * - 가격 유효성 검증 (Request DTO에서 처리)
     * 
     * @param id 업데이트할 상품 ID
     * @param request 상품 업데이트 요청
     * @return 업데이트된 상품
     */
    fun updateProduct(id: Long, request: UpdateProductRequest): ApiResponse<Product> {
        val updatedProduct = productService.updateProduct(id, request.price)
        return ApiResponse.success(updatedProduct, "상품이 성공적으로 수정되었습니다")
    }
    
    /**
     * 상품을 삭제
     * 
     * 상품 삭제 시 다음 비즈니스 규칙을 검증합니다:
     * - 상품 존재 여부 확인 (ProductService에서 처리)
     * 
     * @param id 삭제할 상품 ID
     * @return 삭제 성공 응답
     */
    fun deleteProduct(id: Long): ApiResponse<Unit> {
        productService.deleteById(id)
        return ApiResponse.success(Unit, "상품이 성공적으로 삭제되었습니다")
    }
} 