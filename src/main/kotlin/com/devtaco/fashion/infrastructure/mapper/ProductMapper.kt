package com.devtaco.fashion.infrastructure.mapper

import com.devtaco.fashion.domain.model.Product
import com.devtaco.fashion.domain.model.ProductStatus
import com.devtaco.fashion.infrastructure.persistence.entity.ProductEntity

/**
 * Product Entity와 Domain Model 간의 변환을 담당하는 Mapper
 * 
 * Infrastructure Layer에서 Domain Model과 Entity 간의 변환 로직을 캡슐화합니다.
 * Clean Architecture 원칙에 따라 Domain Layer는 Infrastructure Layer를 모르므로,
 * 변환 로직은 Infrastructure Layer에서 담당합니다.
 */
object ProductMapper {
    
    /**
     * ProductEntity를 Product Domain Model로 변환
     */
    fun toDomain(entity: ProductEntity): Product {
        return Product(
            id = entity.id,
            category = CategoryMapper.toDomain(entity.category),
            brand = BrandMapper.toDomain(entity.brand),
            price = entity.price,
            status = entity.status
        )
    }
    
    /**
     * Product Domain Model을 ProductEntity로 변환
     */
    fun toEntity(domain: Product): ProductEntity {
        return ProductEntity(
            id = domain.id,
            category = CategoryMapper.toEntity(domain.category),
            brand = BrandMapper.toEntity(domain.brand),
            price = domain.price,
            status = domain.status
        )
    }
    
    /**
     * ProductEntity 리스트를 Product Domain Model 리스트로 변환
     */
    fun toDomainList(entities: List<ProductEntity>): List<Product> {
        return entities.map { toDomain(it) }
    }
    
    /**
     * Product Domain Model 리스트를 ProductEntity 리스트로 변환
     */
    fun toEntityList(domains: List<Product>): List<ProductEntity> {
        return domains.map { toEntity(it) }
    }
} 