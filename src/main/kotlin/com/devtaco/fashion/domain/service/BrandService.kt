package com.devtaco.fashion.domain.service

import com.devtaco.fashion.domain.model.Brand
import com.devtaco.fashion.domain.port.BrandRepository
import com.devtaco.fashion.global.common.exceptions.NotFoundException
import com.devtaco.fashion.global.common.exceptions.ValidationException
import org.springframework.stereotype.Service

@Service
class BrandService(
    private val brandRepository: BrandRepository
) {
    fun save(brand: Brand): Brand {
        if (brandRepository.existsByName(brand.name)) {
            throw ValidationException("이미 존재하는 브랜드명입니다: ${brand.name}")
        }
        return brandRepository.save(brand)
    }
    
    fun findById(id: Long): Brand {
        return brandRepository.findById(id) 
            ?: throw NotFoundException("존재하지 않는 브랜드입니다. 브랜드 ID: $id")
    }
    
    fun findByName(name: String): Brand {
        return brandRepository.findByName(name) 
            ?: throw NotFoundException("존재하지 않는 브랜드입니다. 브랜드명: $name")
    }
    
    fun findAll(): List<Brand> {
        return brandRepository.findAll()
    }
    
    fun deleteById(id: Long) {
        if (!brandRepository.existsById(id)) {
            throw NotFoundException("존재하지 않는 브랜드입니다. 브랜드 ID: $id")
        }
        brandRepository.deleteById(id)
    }
    
    fun existsByName(name: String): Boolean {
        return brandRepository.existsByName(name)
    }
} 