package com.devtaco.fashion.global.common.response

/**
 * API 응답 표준 포맷을 제공하는 제네릭 래퍼 클래스입니다.
 * - 성공/실패 여부, 데이터, 메시지, 에러코드 등 공통 응답 구조를 캡슐화합니다.
 * - 컨트롤러, 서비스 등에서 일관된 API 응답을 반환할 때 사용합니다.
 */
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val errorCode: String? = null
) {
    companion object {
        fun <T> success(data: T, message: String? = null): ApiResponse<T> {
            return ApiResponse(true, data, message)
        }
        
        fun <T> error(message: String, errorCode: String? = null): ApiResponse<T> {
            return ApiResponse(false, null, message, errorCode)
        }
    }
} 