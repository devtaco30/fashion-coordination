package com.devtaco.fashion.infrastructure.mapper

import com.devtaco.fashion.domain.model.Brand
import com.devtaco.fashion.infrastructure.persistence.entity.BrandEntity

/**
 * Brand Entity와 Domain Model 간의 변환을 담당하는 Mapper
 */
object BrandMapper {
    
    /**
     * BrandEntity를 Brand Domain Model로 변환
     */
    fun toDomain(entity: BrandEntity): Brand {
        return Brand(
            id = entity.id,
            name = entity.name
        )
    }
    
    /**
     * Brand Domain Model을 BrandEntity로 변환
     */
    fun toEntity(domain: Brand): BrandEntity {
        return BrandEntity(
            id = domain.id,
            name = domain.name
        )
    }
    
    /**
     * BrandEntity 리스트를 Brand Domain Model 리스트로 변환
     */
    fun toDomainList(entities: List<BrandEntity>): List<Brand> {
        return entities.map { toDomain(it) }
    }
    
    /**
     * Brand Domain Model 리스트를 BrandEntity 리스트로 변환
     */
    fun toEntityList(domains: List<Brand>): List<BrandEntity> {
        return domains.map { toEntity(it) }
    }
} 