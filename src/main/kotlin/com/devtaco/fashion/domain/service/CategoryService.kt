package com.devtaco.fashion.domain.service

import com.devtaco.fashion.domain.model.Category
import com.devtaco.fashion.domain.port.CategoryRepository
import com.devtaco.fashion.global.common.exceptions.ValidationException
import com.devtaco.fashion.global.common.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun count(): Long {
        return categoryRepository.count()
    }
    
    fun findByName(name: String): Category {
        return categoryRepository.findByName(name) ?: throw ValidationException("존재하지 않는 카테고리입니다")
    }
    
    fun findById(id: Long): Category {
        return categoryRepository.findById(id) 
            ?: throw NotFoundException("존재하지 않는 카테고리입니다. 카테고리 ID: $id")
    }

    fun findAll(): List<Category> {
        return categoryRepository.findAll()
    }
} 