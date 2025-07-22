package com.devtaco.fashion.domain.port

import com.devtaco.fashion.domain.model.Category

/**
 * 카테고리 저장소 인터페이스
 * 
 * 이 인터페이스는 카테고리와 관련된 기능을 정의합니다.
 * - 카테고리 CRUD 작업
 * - 카테고리명으로 조회
 * - 카테고리 수 조회
 * 
 * Clean Architecture 원칙에 따라 Domain Layer가 Infrastructure Layer에 직접 의존하지 않도록
 * 의존성 역전 원칙을 적용합니다.
 */
interface CategoryRepository {
    fun save(category: Category): Long
    fun saveAll(categories: List<Category>): List<Long>
    fun findById(id: Long): Category?
    fun findByName(name: String): Category?
    fun findAll(): List<Category>
    fun count(): Long
} 