package com.devtaco.fashion.domain.port

import com.devtaco.fashion.domain.model.Product

/**
 * 상품 저장소 인터페이스
 * 
 * 이 인터페이스는 상품과 관련된 기능을 정의합니다.
 * - 코디세트 관련 상품 조회 (카테고리별, 브랜드별 최저가)
 * - 상품 CRUD 작업
 * - 카테고리별 상품 조회
 * 
 * Clean Architecture 원칙에 따라 Domain Layer가 Infrastructure Layer에 직접 의존하지 않도록
 * 의존성 역전 원칙을 적용합니다.
 */
interface ProductRepository {
    fun save(product: Product): Product
    fun findAllByCategory(categoryId: Long): List<Product>
    /**
     * 카테고리별 최저가 상품을 조회
     * 
     * 기능 1번 API를 위한 메서드입니다.
     * 각 카테고리에서 최저가 상품을 조회하여 반환합니다.
     */
    fun findMinPriceCoordiSetEachCategory(): List<Product>
    
    /**
     * 브랜드별, 카테고리별 최저가 상품을 조회
     * 
     * 기능 2번 API를 위한 메서드입니다.
     * 각 브랜드의 각 카테고리에서 최저가 상품을 조회하여 반환합니다.
     */
    fun findMinPriceBrandCoordiSet(): List<Product>
    
    /**
     * 특정 카테고리에서 최저가/최고가 상품을 조회
     * 
     * 기능 3번 API를 위한 메서드입니다.
     */
    fun findMinMaxPriceProductInCategory(categoryId: Long): Pair<Product, Product>
    
    fun existsByCategory(categoryId: Long): Boolean
    fun findById(id: Long): Product?
    fun findByBrandAndCategory(brandId: Long, categoryId: Long): Product?
    fun deleteById(id: Long)
    fun existsById(id: Long): Boolean
    fun findAll(): List<Product>
} 