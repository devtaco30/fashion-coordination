package com.devtaco.fashion.infrastructure.persistence.jpa

import com.devtaco.fashion.infrastructure.persistence.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * JPA용 CategoryEntity 저장소
 */
interface CategoryJpaRepository : JpaRepository<CategoryEntity, Long?> {
    
    @Query("SELECT c FROM CategoryEntity c WHERE c.name = :name")
    fun findByName(@Param("name") name: String): CategoryEntity?
    
    fun findAllByOrderByIdAsc(): List<CategoryEntity>
} 