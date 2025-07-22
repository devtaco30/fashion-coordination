package com.devtaco.fashion.global.common.exceptions

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.http.HttpStatus
import mu.KotlinLogging
import com.devtaco.fashion.global.common.response.ApiResponse

@ControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(NotFoundException::class)
    @ResponseBody
    fun handleNotFoundException(e: NotFoundException): ApiResponse<Nothing> {
        log.error { "NotFoundException: ${e.message}" }
        return ApiResponse.error(e.message ?: "존재하지 않는 리소스입니다", "NOT_FOUND")
    }
    
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    fun handleIllegalArgumentException(e: IllegalArgumentException): ApiResponse<Nothing> {
        return ApiResponse.error(e.message ?: "잘못된 요청입니다", "INVALID_REQUEST")
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseBody
    fun handleValidationException(e: ValidationException): ApiResponse<Nothing> {
        return ApiResponse.error(e.message ?: "유효성 검증에 실패했습니다", "VALIDATION_ERROR")
    }

    @ExceptionHandler(InsufficientDataException::class)
    @ResponseBody
    fun handleInsufficientDataException(e: InsufficientDataException): ApiResponse<Nothing> {
        return ApiResponse.error(e.message ?: "필요한 데이터가 부족합니다", "INSUFFICIENT_DATA")
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException::class)
    fun handleOptimisticLockException(ex: ObjectOptimisticLockingFailureException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(409) // 409 Conflict
            .body(ApiResponse.error("동시 처리 중입니다. 잠시 후 다시 시도해주세요.", "CONCURRENT_MODIFICATION"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val errorMessages = ex.bindingResult.fieldErrors.map { 
            it.defaultMessage 
        }.joinToString(", ")
        
        log.error { "Validation failed: $errorMessages" }
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorMessages, "VALIDATION_ERROR"))
    }
} 