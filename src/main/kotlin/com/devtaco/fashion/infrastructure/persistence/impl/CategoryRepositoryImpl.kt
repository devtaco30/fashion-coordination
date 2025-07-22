package com.devtaco.fashion.infrastructure.persistence.impl

import com.devtaco.fashion.domain.model.Category
import com.devtaco.fashion.domain.port.CategoryRepository
import com.devtaco.fashion.infrastructure.persistence.jpa.CategoryJpaRepository
import com.devtaco.fashion.infrastructure.mapper.CategoryMapper
import org.springframework.stereotype.Repository

/**
 * 카테고리 저장소 구현체
 */
@Repository
class CategoryRepositoryImpl(
    private val categoryJpaRepository: CategoryJpaRepository
) : CategoryRepository {

    override fun save(category: Category): Long {
        val entity = CategoryMapper.toEntity(category)
        val savedEntity = categoryJpaRepository.save(entity)
        return savedEntity.id
    }

    override fun saveAll(categories: List<Category>): List<Long> {
        val entities = CategoryMapper.toEntityList(categories)
        val savedEntities = categoryJpaRepository.saveAll(entities)
        return savedEntities.map { it.id }
    }

    override fun findById(id: Long): Category? {
        return categoryJpaRepository.findById(id).map { CategoryMapper.toDomain(it) }.orElse(null)
    }

    override fun findByName(name: String): Category? {
        return categoryJpaRepository.findByName(name)?.let { CategoryMapper.toDomain(it) }
    }

    override fun findAll(): List<Category> {
        return CategoryMapper.toDomainList(categoryJpaRepository.findAllByOrderByIdAsc())
    }

    override fun count(): Long {
        return categoryJpaRepository.count()
    }
} 