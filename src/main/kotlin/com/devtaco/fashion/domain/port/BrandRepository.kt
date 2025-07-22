package com.devtaco.fashion.domain.port

import com.devtaco.fashion.domain.model.Brand

/**
 * 브랜드 저장소 인터페이스
 * 
 * 이 인터페이스는 브랜드와 관련된 기능을 정의합니다.
 * - 브랜드 CRUD 작업
 * - 브랜드명으로 조회
 * 
 * Clean Architecture 원칙에 따라 Domain Layer가 Infrastructure Layer에 직접 의존하지 않도록
 * 의존성 역전 원칙을 적용합니다.
 */
interface BrandRepository {
    fun save(brand: Brand): Brand
    fun findByName(name: String): Brand?
    fun findById(id: Long): Brand?
    fun findAll(): List<Brand>
    fun existsById(id: Long): Boolean
    fun existsByName(name: String): Boolean
    fun deleteById(id: Long)
} 