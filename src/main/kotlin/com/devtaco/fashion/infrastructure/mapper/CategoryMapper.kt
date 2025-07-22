package com.devtaco.fashion.infrastructure.mapper

import com.devtaco.fashion.domain.model.Category
import com.devtaco.fashion.infrastructure.persistence.entity.CategoryEntity

/**
 * Category Entity와 Domain Model 간의 변환을 담당하는 Mapper
 */
object CategoryMapper {
    
    /**
     * CategoryEntity를 Category Domain Model로 변환
     */
    fun toDomain(entity: CategoryEntity): Category {
        return Category(
            id = entity.id,
            name = entity.name
        )
    }
    
    /**
     * Category Domain Model을 CategoryEntity로 변환
     */
    fun toEntity(domain: Category): CategoryEntity {
        return CategoryEntity(
            id = domain.id,
            name = domain.name
        )
    }
    
    /**
     * CategoryEntity 리스트를 Category Domain Model 리스트로 변환
     */
    fun toDomainList(entities: List<CategoryEntity>): List<Category> {
        return entities.map { toDomain(it) }
    }
    
    /**
     * Category Domain Model 리스트를 CategoryEntity 리스트로 변환
     */
    fun toEntityList(domains: List<Category>): List<CategoryEntity> {
        return domains.map { toEntity(it) }
    }
} 