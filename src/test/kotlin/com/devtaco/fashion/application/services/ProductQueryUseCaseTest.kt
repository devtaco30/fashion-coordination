package com.devtaco.fashion.application.services

import com.devtaco.fashion.domain.model.*
import com.devtaco.fashion.domain.service.ProductService
import com.devtaco.fashion.domain.service.CategoryService
import com.devtaco.fashion.domain.service.BrandService
import com.devtaco.fashion.application.services.ProductQueryUseCase
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import com.devtaco.fashion.global.common.exceptions.ValidationException
import com.devtaco.fashion.global.common.exceptions.InsufficientDataException

@DisplayName("ProductQueryUseCase 테스트")
class ProductQueryUseCaseTest {

    companion object {
        val testCategories = listOf(
            Category(id = 1L, name = "TOP"),
            Category(id = 2L, name = "OUTER"),
            Category(id = 3L, name = "PANTS"),
            Category(id = 4L, name = "SNEAKERS"),
            Category(id = 5L, name = "BAG"),
            Category(id = 6L, name = "HAT"),
            Category(id = 7L, name = "SOCKS"),
            Category(id = 8L, name = "ACCESSORY")
        )
    }
    
    private lateinit var productService: ProductService
    private lateinit var categoryService: CategoryService
    private lateinit var brandService: BrandService

    @BeforeEach
    fun setUp() {
        productService = mock()
        categoryService = mock()
        brandService = mock()
    }

    // ===== 카테고리별 최저가 코디세트 조회 테스트 =====
    
    @DisplayName("카테고리별 최저가 코디세트 조회 - 정상 케이스")
    @Test
    fun get_min_price_coordi_set_by_category_success() {
        // given
        val mockProducts = createMockMinPriceProducts()
        whenever(productService.findMinPriceCoordiSetEachCategory()).thenReturn(mockProducts)
        whenever(categoryService.count()).thenReturn(3L)
        
        val useCase = ProductQueryUseCase(productService, categoryService, brandService)
        
        // when
        val result = useCase.getMinPriceCoordiSetByCategory()
        
        // then
        assertThat(result.data?.coordiSet).hasSize(3)
        assertThat(result.data?.totalPrice).isGreaterThan(0)
        verify(productService).findMinPriceCoordiSetEachCategory()
    }

    @DisplayName("카테고리별 최저가 코디세트 조회 - 카테고리 누락")
    @Test
    fun get_min_price_coordi_set_by_category_missing_category() {
        // given
        val mockProducts = listOf(
            createMockProduct(categoryId = 1, brandId = 1, price = 10000),
            createMockProduct(categoryId = 2, brandId = 2, price = 15000)
            // 카테고리 3번이 누락됨
        )
        
        whenever(productService.findMinPriceCoordiSetEachCategory()).thenReturn(mockProducts)
        whenever(categoryService.count()).thenReturn(3L)
        
        val useCase = ProductQueryUseCase(productService, categoryService, brandService)
        
        // when & then
        assertThatThrownBy { useCase.getMinPriceCoordiSetByCategory() }
            .isInstanceOf(InsufficientDataException::class.java)
            .hasMessage("모든 카테고리를 포함하는 코디세트를 구성할 수 없습니다. 일부 카테고리에 상품이 없을 수 있습니다.")
    }

    // ===== 브랜드별 최저가 코디세트 조회 테스트 =====
    
    @DisplayName("브랜드별 최저가 코디세트 조회 - 정상 케이스")
    @Test
    fun get_lowest_price_coordi_set_by_brand_success() {
        // given
        val mockProducts = createMockMinPriceBrandProducts()
        
        whenever(productService.findMinPriceBrandCoordiSet()).thenReturn(mockProducts)
        whenever(categoryService.count()).thenReturn(3L)
        
        val useCase = ProductQueryUseCase(productService, categoryService, brandService)
        
        // when
        val result = useCase.getLowestPriceCoordiSetByBrand()
        
        // then
        assertThat(result.data?.category).hasSize(3)
        assertThat(result.data?.totalPrice).isGreaterThan(0)
        verify(productService).findMinPriceBrandCoordiSet()
    }

    @DisplayName("브랜드별 최저가 코디세트 조회 - 데이터 없음")
    @Test
    fun get_lowest_price_coordi_set_by_brand_empty_data() {
        // given
        whenever(productService.findMinPriceBrandCoordiSet()).thenThrow(
            InsufficientDataException("조건을 만족하는 최저가 브랜드 코디세트가 없습니다")
        )
        whenever(categoryService.count()).thenReturn(3L)
        
        val useCase = ProductQueryUseCase(productService, categoryService, brandService)
        
        // when & then
        assertThatThrownBy { useCase.getLowestPriceCoordiSetByBrand() }
            .isInstanceOf(InsufficientDataException::class.java)
            .hasMessage("조건을 만족하는 최저가 브랜드 코디세트가 없습니다")
    }

    @DisplayName("브랜드별 최저가 코디세트 조회 - 카테고리 누락")
    @Test
    fun get_lowest_price_coordi_set_by_brand_missing_category() {
        // given
        val mockProducts = listOf(
            createMockProduct(categoryId = 1, brandId = 1, price = 10000),
            createMockProduct(categoryId = 2, brandId = 1, price = 15000)
            // 카테고리 3번이 누락됨
        )
        
        whenever(productService.findMinPriceBrandCoordiSet()).thenReturn(mockProducts)
        whenever(categoryService.count()).thenReturn(3L)
        
        val useCase = ProductQueryUseCase(productService, categoryService, brandService)
        
        // when & then
        assertThatThrownBy { useCase.getLowestPriceCoordiSetByBrand() }
            .isInstanceOf(InsufficientDataException::class.java)
            .hasMessage("모든 카테고리를 포함하는 코디세트를 구성할 수 없습니다. 일부 카테고리에 상품이 없을 수 있습니다.")
    }

    // ===== 카테고리별 최저/최고가 브랜드 조회 테스트 =====
    
    @DisplayName("카테고리별 최저/최고가 브랜드 조회 - 정상 케이스")
    @Test
    fun get_min_max_price_by_category_success() {
        // given
        val targetCategory = testCategories.first { it.name == "TOP" }
        val mockProducts = listOf(
            createMockProduct(categoryId = 1, brandId = 1, price = 5000),  // 최저가
            createMockProduct(categoryId = 1, brandId = 2, price = 15000),
            createMockProduct(categoryId = 1, brandId = 3, price = 95000)  // 최고가
        )
        
        whenever(categoryService.findByName("TOP")).thenReturn(targetCategory)
        whenever(productService.findAllByCategory(targetCategory.id)).thenReturn(mockProducts)
        whenever(productService.findMinMaxPriceProductInCategory(targetCategory.id)).thenReturn(
            Pair(mockProducts[0], mockProducts[2])
        )
        
        val useCase = ProductQueryUseCase(productService, categoryService, brandService)
        
        // when
        val result = useCase.getMinMaxPriceByCategory("TOP")
        
        // then
        assertThat(result.data?.category).isEqualTo(targetCategory.name)
        assertThat(result.data?.minPrice?.get(0)?.price).isEqualTo(5000)
        assertThat(result.data?.maxPrice?.get(0)?.price).isEqualTo(95000)
        assertThat(result.data?.minPrice?.get(0)?.price).isLessThan(result.data?.maxPrice?.get(0)?.price)
    }

    @DisplayName("존재하지 않는 카테고리로 조회 시 예외 발생")
    @Test
    fun get_min_max_price_by_category_category_not_found() {
        // given
        whenever(categoryService.findByName("NOT_EXIST_CATEGORY")).thenThrow(
            ValidationException("존재하지 않는 카테고리입니다")
        )
        
        val useCase = ProductQueryUseCase(productService, categoryService, brandService)

        // when & then
        assertThatThrownBy { 
            useCase.getMinMaxPriceByCategory("NOT_EXIST_CATEGORY") 
        }.isInstanceOf(ValidationException::class.java)
            .hasMessageContaining("존재하지 않는 카테고리입니다")
    }

    @DisplayName("카테고리에 상품이 없을 때 예외 발생")
    @Test
    fun get_min_max_price_by_category_no_products_in_category() {
        // given
        val targetCategory = testCategories.first { it.name == "TOP" }
        
        whenever(categoryService.findByName("TOP")).thenReturn(targetCategory)
        whenever(productService.findMinMaxPriceProductInCategory(targetCategory.id)).thenThrow(
            InsufficientDataException("해당 카테고리에 상품이 없습니다. 카테고리 ID: ${targetCategory.id}")
        )

        val useCase = ProductQueryUseCase(productService, categoryService, brandService)

        // when & then
        assertThatThrownBy { 
            useCase.getMinMaxPriceByCategory("TOP") 
        }.isInstanceOf(InsufficientDataException::class.java)
            .hasMessageContaining("해당 카테고리에 상품이 없습니다")
    }

    // ===== 헬퍼 메서드들 =====
    
    private fun createMockProduct(categoryId: Long, brandId: Long, price: Int): Product {
        val categoryName = when (categoryId) {
            1L -> "TOP"
            2L -> "OUTER"
            3L -> "PANTS"
            else -> "CATEGORY_$categoryId"
        }
        return Product(
            id = (categoryId * 100 + brandId).toLong(),
            category = Category(id = categoryId, name = categoryName),
            brand = Brand(id = brandId, name = "BRAND_$brandId"),
            price = price,
            status = ProductStatus.NORMAL
        )
    }
    
    private fun createMockMinPriceProducts(): List<Product> {
        return listOf(
            createMockProduct(categoryId = 1, brandId = 1, price = 10000),
            createMockProduct(categoryId = 2, brandId = 2, price = 15000),
            createMockProduct(categoryId = 3, brandId = 3, price = 20000)
        )
    }
    
    private fun createMockMinPriceBrandProducts(): List<Product> {
        return listOf(
            createMockProduct(categoryId = 1, brandId = 1, price = 10000),
            createMockProduct(categoryId = 2, brandId = 1, price = 15000),
            createMockProduct(categoryId = 3, brandId = 1, price = 20000)
        )
    }
}