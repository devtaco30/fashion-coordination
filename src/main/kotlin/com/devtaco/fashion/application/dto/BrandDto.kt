package com.devtaco.fashion.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * 브랜드 관련 요청 DTO
 * 
 * NOTE: Clean Archtiecture 를 지향한다면, Command 와 Web 계층인 DTO 를 분리하는 것이 좋지만,
 * 현재 과제의 규모가 작아, 분리하지 않고 하나의 DTO 클래스에 모두 정의합니다.
 */
data class CreateBrandRequest(
    @field:NotBlank(message = "브랜드명은 필수입니다")
    @field:Size(min = 1, max = 100, message = "브랜드명은 1자 이상 100자 이하여야 합니다")
    val name: String
)

data class UpdateBrandRequest(
    @field:NotBlank(message = "브랜드명은 필수입니다")
    @field:Size(min = 1, max = 100, message = "브랜드명은 1자 이상 100자 이하여야 합니다")
    val name: String
)