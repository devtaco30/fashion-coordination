package com.devtaco.fashion.global.common.exceptions

class NotFoundException(
    message: String,
    val errorCode: String = "NOT_FOUND"
) : RuntimeException(message) 