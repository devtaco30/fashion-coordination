package com.devtaco.fashion.infrastructure.persistence.jpa

import com.devtaco.fashion.infrastructure.persistence.entity.BrandEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA용 BrandEntity 저장소
 */
interface BrandJpaRepository : JpaRepository<BrandEntity, Long?> {
    fun findByName(name: String): Optional<BrandEntity>
    
    fun existsByName(name: String): Boolean
    
    fun findAllByOrderByIdAsc(): List<BrandEntity>
}