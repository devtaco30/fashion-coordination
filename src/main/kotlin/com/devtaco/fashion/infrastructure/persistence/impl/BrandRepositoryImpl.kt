package com.devtaco.fashion.infrastructure.persistence.impl

import com.devtaco.fashion.domain.model.Brand
import com.devtaco.fashion.domain.port.BrandRepository
import com.devtaco.fashion.infrastructure.persistence.jpa.BrandJpaRepository
import com.devtaco.fashion.infrastructure.mapper.BrandMapper
import org.springframework.stereotype.Repository

@Repository
class BrandRepositoryImpl(
    private val jpaRepository: BrandJpaRepository,
) : BrandRepository {

    override fun save(brand: Brand): Brand {
        val entity = BrandMapper.toEntity(brand)
        val savedEntity = jpaRepository.save(entity)
        return BrandMapper.toDomain(savedEntity)
    }

    override fun findByName(name: String): Brand? {
        return jpaRepository.findByName(name).map { BrandMapper.toDomain(it) }.orElse(null)
    }

    override fun findById(id: Long): Brand? {
        return jpaRepository.findById(id).map { BrandMapper.toDomain(it) }.orElse(null)
    }

    override fun findAll(): List<Brand> {
        return BrandMapper.toDomainList(jpaRepository.findAllByOrderByIdAsc())
    }

    
    override fun existsById(id: Long): Boolean {
        return jpaRepository.existsById(id)
    }
    
    override fun existsByName(name: String): Boolean {
        return jpaRepository.existsByName(name)
    }
    
    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }
} 