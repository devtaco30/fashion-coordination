package com.devtaco.fashion.global.common.exceptions

class ValidationException(
    message: String,
    val errorCode: String = "VALIDATION_ERROR"
) : RuntimeException(message) 