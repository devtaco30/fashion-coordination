package com.devtaco.fashion.domain.service

import com.devtaco.fashion.domain.model.Product
import com.devtaco.fashion.domain.port.ProductRepository
import com.devtaco.fashion.global.common.exceptions.InsufficientDataException
import com.devtaco.fashion.global.common.exceptions.NotFoundException
import org.springframework.stereotype.Service

/**
 * 상품 도메인 서비스
 * 
 * 이 서비스는 상품과 관련된 비즈니스 로직을 캡슐화합니다.
 * - 상품 조회 및 검증
 * - 상품 생성, 수정, 삭제
 * - 코디세트 관련 상품 조회
 * 
 * Clean Architecture 원칙에 따라 Application Layer가 Infrastructure Layer에 직접 의존하지 않도록
 * Repository 위임과 도메인 예외 처리를 담당합니다.
 */
@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    /**
     * 카테고리별 최저가 상품을 조회하여 반환
     * 
     * 기능 1번 API를 위한 메서드입니다.
     * Repository에서 이미 카테고리별 최저가 상품을 조회한 결과를 검증하고 반환합니다.
     */
    fun findMinPriceCoordiSetEachCategory(): List<Product> {
        val products = productRepository.findMinPriceCoordiSetEachCategory()
        if (products.isEmpty()) {
            throw InsufficientDataException("조건을 만족하는 최저가 코디세트가 없습니다")
        }
        return products
    }
    
    /**
     * 브랜드별, 카테고리별 최저가 상품을 조회하여 반환
     * 
     * 기능 2번 API를 위한 메서드입니다.
     * Repository에서 브랜드별, 카테고리별 최저가 상품을 조회한 결과를 검증하고 반환합니다.
     */
    fun findMinPriceBrandCoordiSet(): List<Product> {
        val products = productRepository.findMinPriceBrandCoordiSet()
        if (products.isEmpty()) {
            throw InsufficientDataException("조건을 만족하는 최저가 브랜드 코디세트가 없습니다")
        }
        return products
    }
    
    /**
     * 특정 카테고리의 모든 상품을 조회하여 반환
     * 
     * 기능 3번 API를 위한 메서드입니다.
     */
    fun findAllByCategory(categoryId: Long): List<Product> {
        val products = productRepository.findAllByCategory(categoryId)
        if (products.isEmpty()) {
            throw InsufficientDataException("해당 카테고리에 판매 가능한 상품이 없습니다. 카테고리 ID: $categoryId")
        }
        return products
    }
    
    /**
     * 특정 카테고리에서 최저가/최고가 상품을 조회하여 반환
     * 
     * 기능 3번 API를 위한 메서드입니다.
     */
    fun findMinMaxPriceProductInCategory(categoryId: Long): Pair<Product, Product> {
        if (!productRepository.existsByCategory(categoryId)) {
            throw InsufficientDataException("해당 카테고리에 상품이 없습니다.")
        }
        return productRepository.findMinMaxPriceProductInCategory(categoryId)
    }
    
    fun save(product: Product): Product {
        return productRepository.save(product)
    }
    
    fun findById(id: Long): Product {
        return productRepository.findById(id) 
            ?: throw NotFoundException("존재하지 않는 상품입니다. 상품 ID: $id")
    }
    
    fun findByBrandAndCategory(brandId: Long, categoryId: Long): Product? {
        return productRepository.findByBrandAndCategory(brandId, categoryId)
    }

    fun deleteById(id: Long) {
        if (!productRepository.existsById(id)) {
            throw NotFoundException("존재하지 않는 상품입니다. 상품 ID: $id")
        }
        productRepository.deleteById(id)
    }
 

    fun existsById(id: Long): Boolean {
        return productRepository.existsById(id)
    }
    
    fun updateProduct(id: Long, price: Int): Product {
        val product = findById(id)
        product.price = price
        save(product)
        return product
    }

    fun findAll(): List<Product> {
        return productRepository.findAll()
    }
} 