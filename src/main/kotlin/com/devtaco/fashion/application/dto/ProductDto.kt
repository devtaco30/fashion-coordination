package com.devtaco.fashion.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

/**
 * 상품 관련 요청 DTO
 * 
 * NOTE: Clean Archtiecture 를 지향한다면, Command 와 Web 계층인 DTO 를 분리하는 것이 좋지만,
 * 현재 과제의 규모가 작아, 분리하지 않고 하나의 DTO 클래스에 모두 정의합니다.
 */
data class CreateProductRequest(
    @field:Positive(message = "브랜드 ID는 0보다 커야 합니다")
    val brandId: Long,
    
    @field:Positive(message = "카테고리 ID는 0보다 커야 합니다")
    val categoryId: Long,
    
    @field:Positive(message = "가격은 0보다 커야 합니다")
    val price: Int
) 


/**
 * 상품 수정 요청 DTO
 * 
 * 설계 원칙:
 * - 가격만 수정 가능 (브랜드/카테고리는 수정 불가)
 * - 브랜드/카테고리를 변경하려면 삭제 후 재등록 필요
 * - 이유: 브랜드/카테고리 변경은 완전히 다른 상품이므로 데이터 무결성 보장
 */
data class UpdateProductRequest(
    @field:NotNull(message = "가격은 필수입니다")
    @field:Positive(message = "가격은 0보다 커야 합니다")
    val price: Int
)