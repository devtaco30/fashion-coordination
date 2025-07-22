package com.devtaco.fashion.infrastructure.persistence.impl

import com.devtaco.fashion.global.common.exceptions.InsufficientDataException
import com.devtaco.fashion.domain.model.Product
import com.devtaco.fashion.domain.model.ProductStatus
import com.devtaco.fashion.domain.port.ProductRepository
import com.devtaco.fashion.infrastructure.persistence.jpa.ProductJpaRepository
import com.devtaco.fashion.infrastructure.mapper.ProductMapper
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val jpaRepository: ProductJpaRepository,
) : ProductRepository {

    override fun save(product: Product): Product {
        val entity = ProductMapper.toEntity(product)
        val savedEntity = jpaRepository.save(entity)
        return ProductMapper.toDomain(savedEntity)
    }

    override fun findMinPriceCoordiSetEachCategory(): List<Product> {
        return ProductMapper.toDomainList(jpaRepository.findMinPriceProductsByAllCategories())
    }

    override fun findAllByCategory(categoryId: Long): List<Product> {
        return ProductMapper.toDomainList(jpaRepository.findAllByCategory(categoryId))
    }

    override fun findMinPriceBrandCoordiSet(): List<Product> {
        return ProductMapper.toDomainList(jpaRepository.findBrandCategoryMinProducts())
    }
    
    override fun findMinMaxPriceProductInCategory(categoryId: Long): Pair<Product, Product> {
        val minProduct = jpaRepository.findFirstByCategoryIdAndStatusOrderByPriceAscIdAsc(categoryId, ProductStatus.NORMAL).orElseThrow { InsufficientDataException("카테고리에 상품이 없습니다") }
        val maxProduct = jpaRepository.findFirstByCategoryIdAndStatusOrderByPriceDescIdAsc(categoryId, ProductStatus.NORMAL).orElseThrow { InsufficientDataException("카테고리에 상품이 없습니다") }
        return Pair(ProductMapper.toDomain(minProduct), ProductMapper.toDomain(maxProduct))
    }
    
    override fun findById(id: Long): Product? {
        return jpaRepository.findById(id).map { ProductMapper.toDomain(it) }.orElse(null)
    }
    
    override fun findByBrandAndCategory(brandId: Long, categoryId: Long): Product? {
        return jpaRepository.findMinPriceByBrandAndCategory(brandId, categoryId)
            .map { ProductMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }
    
    override fun existsById(id: Long): Boolean {
        return jpaRepository.existsById(id)
    }
    
    override fun existsByCategory(categoryId: Long): Boolean {
        return jpaRepository.existsByCategoryId(categoryId)
    }

    override fun findAll(): List<Product> {
        return ProductMapper.toDomainList(jpaRepository.findAll())
    }
} 