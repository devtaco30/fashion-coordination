package com.devtaco.fashion.application.services

import com.devtaco.fashion.domain.model.*
import com.devtaco.fashion.domain.service.BrandService
import com.devtaco.fashion.domain.service.CategoryService
import com.devtaco.fashion.domain.service.ProductService
import com.devtaco.fashion.global.common.exceptions.NotFoundException
import com.devtaco.fashion.global.common.exceptions.ValidationException
import com.devtaco.fashion.application.dto.CreateBrandRequest
import com.devtaco.fashion.application.dto.CreateProductRequest
import com.devtaco.fashion.application.dto.UpdateProductRequest
import com.devtaco.fashion.application.dto.UpdateBrandRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.never
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.mockito.kotlin.any

@DisplayName("ProductManagementUseCase 테스트")
class ProductManagementUseCaseTest {
    
    private lateinit var productManagementUseCase: ProductManagementUseCase
    private lateinit var brandService: BrandService
    private lateinit var categoryService: CategoryService
    private lateinit var productService: ProductService
    
    @BeforeEach
    fun setUp() {
        brandService = mock()
        categoryService = mock()
        productService = mock()
        productManagementUseCase = ProductManagementUseCase(brandService, categoryService, productService)
    }
    
    @DisplayName("브랜드 생성 성공")
    @Test
    fun create_brand_success() {
        // given
        val request = CreateBrandRequest("테스트")
        val expectedBrand = Brand(name = "테스트")
        whenever(brandService.save(expectedBrand)).thenReturn(expectedBrand)
        
        // when
        val result = productManagementUseCase.createBrand(request)
        
        // then
        assertThat(result.success).isTrue()
        assertThat(result.message).isEqualTo("브랜드가 성공적으로 생성되었습니다")
        assertThat(result.data).isEqualTo(expectedBrand)
        verify(brandService).save(expectedBrand)
    }

    @DisplayName("브랜드 수정 성공")
    @Test
    fun update_brand_success() {
        // given
        val id = 1L
        val request = UpdateBrandRequest("수정")
        val existingBrand = Brand(id = 1L, name = "테스트")
        val updatedBrand = existingBrand.copy(name = "수정")
        whenever(brandService.findById(id)).thenReturn(existingBrand)
        whenever(brandService.save(updatedBrand)).thenReturn(updatedBrand)
        
        // when
        val result = productManagementUseCase.updateBrand(id, request)
        
        // then
        assertThat(result.success).isTrue()
        assertThat(result.message).isEqualTo("브랜드가 성공적으로 수정되었습니다")
        assertThat(result.data).isEqualTo(updatedBrand)
        verify(brandService).findById(id)
        verify(brandService).save(updatedBrand)
    }

    @DisplayName("브랜드 삭제 성공")
    @Test
    fun delete_brand_success() {
        // given
        val id = 1L
        
        // when
        val result = productManagementUseCase.deleteBrand(id)
        
        // then
        assertThat(result.success).isTrue()
        assertThat(result.message).isEqualTo("브랜드가 성공적으로 삭제되었습니다")
        verify(brandService).deleteById(id)
    }

    @DisplayName("상품 생성 성공")
    @Test
    fun create_product_success() {
        // given
        val request = CreateProductRequest(brandId = 1L, categoryId = 1L, price = 10000)
        val brand = Brand(id = 1L, name = "테스트")
        val category = Category(id = 1L, name = "테스트")
        val expectedProduct = Product(
            category = category,
            brand = brand,
            price = 10000,
            status = ProductStatus.NORMAL
        )
        whenever(brandService.findById(1L)).thenReturn(brand)
        whenever(categoryService.findById(1L)).thenReturn(category)
        whenever(productService.save(expectedProduct)).thenReturn(expectedProduct)
        
        // when
        val result = productManagementUseCase.createProduct(request)
        
        // then
        assertThat(result.success).isTrue()
        assertThat(result.message).isEqualTo("상품이 성공적으로 생성되었습니다")
        assertThat(result.data).isEqualTo(expectedProduct)
        verify(brandService).findById(1L)
        verify(categoryService).findById(1L)
        verify(productService).save(expectedProduct)
    }

    @DisplayName("상품 생성 실패 - 브랜드가 존재하지 않음")
    @Test
    fun create_product_brand_not_found() {
        // given
        val request = CreateProductRequest(brandId = 999L, categoryId = 1L, price = 10000)
        whenever(brandService.findById(999L)).thenThrow(NotFoundException("존재하지 않는 브랜드입니다. 브랜드 ID: 999"))
        
        // when & then
        assertThatThrownBy { productManagementUseCase.createProduct(request) }
            .isInstanceOf(NotFoundException::class.java)
            .hasMessage("존재하지 않는 브랜드입니다. 브랜드 ID: 999")
        verify(brandService).findById(999L)
        verify(categoryService, never()).findById(1L)
        verify(productService, never()).save(any())
    }

    @DisplayName("상품 생성 실패 - 카테고리가 존재하지 않음")
    @Test
    fun create_product_category_not_found() {
        // given
        val request = CreateProductRequest(brandId = 1L, categoryId = 999L, price = 10000)
        val brand = Brand(id = 1L, name = "테스트")
        whenever(brandService.findById(1L)).thenReturn(brand)
        whenever(categoryService.findById(999L)).thenThrow(NotFoundException("존재하지 않는 카테고리입니다. 카테고리 ID: 999"))
        
        // when & then
        assertThatThrownBy { productManagementUseCase.createProduct(request) }
            .isInstanceOf(NotFoundException::class.java)
            .hasMessage("존재하지 않는 카테고리입니다. 카테고리 ID: 999")
        verify(brandService).findById(1L)
        verify(categoryService).findById(999L)
        verify(productService, never()).save(any())
    }

    @DisplayName("상품 수정 성공")
    @Test
    fun update_product_success() {
        // given
        val id = 1L
        val request = UpdateProductRequest(price = 15000)
        val updatedProduct = Product(
            id = 1L,
            category = Category(id = 1L, name = "테스트"),
            brand = Brand(id = 1L, name = "테스트"),
            price = 15000,
            status = ProductStatus.NORMAL
        )
        whenever(productService.updateProduct(id, 15000)).thenReturn(updatedProduct)
        
        // when
        val result = productManagementUseCase.updateProduct(id, request)
        
        // then
        assertThat(result.success).isTrue()
        assertThat(result.message).isEqualTo("상품이 성공적으로 수정되었습니다")
        assertThat(result.data).isEqualTo(updatedProduct)
        verify(productService).updateProduct(id, 15000)
    }

    @DisplayName("상품 삭제 성공")
    @Test
    fun delete_product_success() {
        // given
        val id = 1L
        
        // when
        val result = productManagementUseCase.deleteProduct(id)
        
        // then
        assertThat(result.success).isTrue()
        assertThat(result.message).isEqualTo("상품이 성공적으로 삭제되었습니다")
        verify(productService).deleteById(id)
    }
} 