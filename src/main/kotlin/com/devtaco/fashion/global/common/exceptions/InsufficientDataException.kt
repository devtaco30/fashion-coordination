package com.devtaco.fashion.global.common.exceptions

class InsufficientDataException(
    message: String,
    val errorCode: String = "INSUFFICIENT_DATA"
) : RuntimeException(message) 