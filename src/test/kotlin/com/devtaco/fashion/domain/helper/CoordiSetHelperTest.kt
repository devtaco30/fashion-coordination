package com.devtaco.fashion.domain.helper

import com.devtaco.fashion.domain.model.Brand
import com.devtaco.fashion.domain.model.Category
import com.devtaco.fashion.domain.model.Product
import com.devtaco.fashion.domain.model.ProductStatus
import com.devtaco.fashion.global.common.exceptions.InsufficientDataException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CoordiSetHelperTest {

    // ===== 카테고리별 최저가 코디세트(createLowestPriceCoordiSet) 생성 테스트 =====

     @Test
     @DisplayName("createLowestPriceCoordiSet - 입력된 상품 목록으로 코디세트 생성")
     fun success_lowest_price_coordi_set_with_input_products() {
         // given
         val brandA = Brand(id = 1L, name = "브랜드A")
         val brandB = Brand(id = 2L, name = "브랜드B")
         
         val category1 = Category(id = 1L, name = "상의")
         val category2 = Category(id = 2L, name = "하의")
         val category3 = Category(id = 3L, name = "신발")
         
         val products = listOf(
             Product(id = 1L, category = category1, brand = brandA, price = 15000, status = ProductStatus.NORMAL),
             Product(id = 2L, category = category2, brand = brandB, price = 12000, status = ProductStatus.NORMAL),
             Product(id = 3L, category = category3, brand = brandA, price = 18000, status = ProductStatus.NORMAL)
         )
         val requiredCategoryCount = 3L
 
         // when
         val result = CoordiSetHelper.createLowestPriceCoordiSet(products, requiredCategoryCount)
 
         // then
         assertThat(result).isNotNull
         assertThat(result.coordiSet).hasSize(3)
         assertThat(result.totalPrice).isEqualTo(45000) // 15000 + 12000 + 18000
         assertThat(result.coordiSet).anyMatch { it.brand == "브랜드A" && it.category == "상의" }
         assertThat(result.coordiSet).anyMatch { it.brand == "브랜드B" && it.category == "하의" }
         assertThat(result.coordiSet).anyMatch { it.brand == "브랜드A" && it.category == "신발" }
     }
 
     @Test
     @DisplayName("createLowestPriceCoordiSet - 필수 카테고리 수보다 적은 카테고리가 있을 때 예외 발생")
     fun fail_lowest_price_coordi_set_when_insufficient_categories_throws_exception() {
         // given
         val brand = Brand(id = 1L, name = "테스트브랜드")
         val category1 = Category(id = 1L, name = "상의")
         val category2 = Category(id = 2L, name = "하의")
         
         val products = listOf(
             Product(id = 1L, category = category1, brand = brand, price = 10000, status = ProductStatus.NORMAL),
             Product(id = 2L, category = category2, brand = brand, price = 15000, status = ProductStatus.NORMAL)
         )
         val requiredCategoryCount = 3L
 
         // when & then
         assertThatThrownBy {
             CoordiSetHelper.createLowestPriceCoordiSet(products, requiredCategoryCount)
         }.isInstanceOf(InsufficientDataException::class.java)
             .hasMessage("모든 카테고리를 포함하는 코디세트를 구성할 수 없습니다. 일부 카테고리에 상품이 없을 수 있습니다.")
     }
 
     @Test
     @DisplayName("createLowestPriceCoordiSet - 상품이 없을 때 예외 발생")
     fun fail_lowest_price_coordi_set_when_no_products_throws_exception() {
         // given
         val products = emptyList<Product>()
         val requiredCategoryCount = 3L
 
         // when & then
         assertThatThrownBy {
             CoordiSetHelper.createLowestPriceCoordiSet(products, requiredCategoryCount)
         }.isInstanceOf(InsufficientDataException::class.java)
             .hasMessage("모든 카테고리를 포함하는 코디세트를 구성할 수 없습니다. 일부 카테고리에 상품이 없을 수 있습니다.")
     }

    // ===== 브랜드별 최저가 코디세트(createLowestPriceBrandCoordiSet) 생성 테스트 =====
    @Test
    @DisplayName("createLowestPriceBrandCoordiSet - 브랜드 2개, 카테고리 3개로 구성된 상품 목록에서 최저가 브랜드 선택")
    fun success_lowest_price_brand_coordi_set_when_two_brands_with_three_categories() {
        // given
        val brandA = Brand(id = 1L, name = "A")
        val brandB = Brand(id = 2L, name = "B")
        
        val category1 = Category(id = 1L, name = "상의")
        val category2 = Category(id = 2L, name = "하의")
        val category3 = Category(id = 3L, name = "신발")
        
        // Repository에서 이미 브랜드별, 카테고리별 최저가 상품 1개씩만 가져옴
        val products = listOf(
            // 브랜드A 상품들 (비싼 브랜드)
            Product(id = 1L, category = category1, brand = brandA, price = 15000, status = ProductStatus.NORMAL),
            Product(id = 2L, category = category2, brand = brandA, price = 20000, status = ProductStatus.NORMAL),
            Product(id = 3L, category = category3, brand = brandA, price = 25000, status = ProductStatus.NORMAL),
            
            // 브랜드B 상품들 (싼 브랜드)
            Product(id = 4L, category = category1, brand = brandB, price = 10000, status = ProductStatus.NORMAL),
            Product(id = 5L, category = category2, brand = brandB, price = 12000, status = ProductStatus.NORMAL),
            Product(id = 6L, category = category3, brand = brandB, price = 18000, status = ProductStatus.NORMAL)
        )
        val requiredCategoryCount = 3L

        // when
        val result = CoordiSetHelper.createLowestPriceBrandCoordiSet(products, requiredCategoryCount)

        // then
        assertThat(result).isNotNull
        assertThat(result.brand).isEqualTo("B")
        assertThat(result.category).hasSize(3)
        assertThat(result.totalPrice).isEqualTo(40000) // 10000 + 12000 + 18000
        assertThat(result.category.map { it.category }).containsExactlyInAnyOrder("상의", "하의", "신발")
    }

    @Test
    @DisplayName("createLowestPriceBrandCoordiSet - 요구되는 카테고리 수보다 적은 카테고리가 있을 때 예외 발생")
    fun fail_lowest_price_brand_coordi_set_when_insufficient_categories_throws_exception() {
        // given
        val brand = Brand(id = 1L, name = "테스트브랜드")
        val category1 = Category(id = 1L, name = "상의")
        val category2 = Category(id = 2L, name = "하의")
        
        val products = listOf(
            Product(id = 1L, category = category1, brand = brand, price = 10000, status = ProductStatus.NORMAL),
            Product(id = 2L, category = category2, brand = brand, price = 15000, status = ProductStatus.NORMAL)
        )
        val requiredCategoryCount = 3L

        // when & then
        assertThatThrownBy {
            CoordiSetHelper.createLowestPriceBrandCoordiSet(products, requiredCategoryCount)
        }.isInstanceOf(InsufficientDataException::class.java)
            .hasMessage("모든 카테고리를 포함하는 코디세트를 구성할 수 없습니다. 일부 카테고리에 상품이 없을 수 있습니다.")
    }

    @Test
    @DisplayName("createLowestPriceBrandCoordiSet - 상품이 없을 때 예외 발생")
    fun fail_lowest_price_brand_coordi_set_when_no_products_throws_exception() {
        // given
        val products = emptyList<Product>()
        val requiredCategoryCount = 3L

        // when & then
        assertThatThrownBy {
            CoordiSetHelper.createLowestPriceBrandCoordiSet(products, requiredCategoryCount)
        }.isInstanceOf(InsufficientDataException::class.java)
            .hasMessage("모든 카테고리를 포함하는 코디세트를 구성할 수 없습니다. 일부 카테고리에 상품이 없을 수 있습니다.")
    }

    @Test
    @DisplayName("createLowestPriceBrandCoordiSet - 모든 카테고리를 포함하는 브랜드가 없으면 예외 발생")
    fun fail_lowest_price_brand_coordi_set_when_no_brand_has_all_categories_throws_exception() {
        // given
        val brandA = Brand(id = 1L, name = "A")
        val brandB = Brand(id = 2L, name = "B")
        
        val category1 = Category(id = 1L, name = "상의")
        val category2 = Category(id = 2L, name = "하의")
        val category3 = Category(id = 3L, name = "신발")
        
        val products = listOf(
            // A - 상의, 하의만
            Product(id = 1L, category = category1, brand = brandA, price = 10000, status = ProductStatus.NORMAL),
            Product(id = 2L, category = category2, brand = brandA, price = 12000, status = ProductStatus.NORMAL),
            
            // B - 하의, 신발만
            Product(id = 3L, category = category2, brand = brandB, price = 9000, status = ProductStatus.NORMAL),
            Product(id = 4L, category = category3, brand = brandB, price = 15000, status = ProductStatus.NORMAL)
        )
        val requiredCategoryCount = 3L

        // when & then
        assertThatThrownBy {
            CoordiSetHelper.createLowestPriceBrandCoordiSet(products, requiredCategoryCount)
        }.isInstanceOf(InsufficientDataException::class.java)
            .hasMessage("모든 카테고리를 포함하는 코디세트를 구성할 수 없습니다. 일부 카테고리에 상품이 없을 수 있습니다.")
    }

    @Test
    @DisplayName("createLowestPriceBrandCoordiSet - 동일한 총 가격의 브랜드가 있을 때 브랜드 ID가 작은 브랜드 선택")
    fun success_lowest_price_brand_coordi_set_when_same_total_price_selects_smaller_brand_id() {
        // given
        val brandA = Brand(id = 1L, name = "A") // 브랜드 ID가 작음
        val brandB = Brand(id = 2L, name = "B") // 브랜드 ID가 큼
        
        val category1 = Category(id = 1L, name = "상의")
        val category2 = Category(id = 2L, name = "하의")
        
        val products = listOf(
            // 브랜드A (총 30000원, ID: 1)
            Product(id = 1L, category = category1, brand = brandA, price = 10000, status = ProductStatus.NORMAL),
            Product(id = 2L, category = category2, brand = brandA, price = 20000, status = ProductStatus.NORMAL),
            
            // 브랜드B (총 30000원, ID: 2) - 동일한 가격이지만 ID가 큼
            Product(id = 3L, category = category1, brand = brandB, price = 15000, status = ProductStatus.NORMAL),
            Product(id = 4L, category = category2, brand = brandB, price = 15000, status = ProductStatus.NORMAL)
        )
        val requiredCategoryCount = 2L

        // when
        val result = CoordiSetHelper.createLowestPriceBrandCoordiSet(products, requiredCategoryCount)

        // then
        assertThat(result).isNotNull
        assertThat(result.brand).isEqualTo("A") // 브랜드 ID가 작은 브랜드 선택
        assertThat(result.category).hasSize(2)
        assertThat(result.totalPrice).isEqualTo(30000)
    }
}
 