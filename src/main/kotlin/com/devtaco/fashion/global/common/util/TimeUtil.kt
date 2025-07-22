package com.devtaco.fashion.global.common.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 시간 관련 유틸리티
 */
object TimeUtil {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    
    fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(formatter)
    }
    
    fun parseDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString, formatter)
    }
    }

    /**
 * 가격 포맷팅 유틸리티
 */
object PriceUtil {
    /**
     * 정수 가격을 콤마가 포함된 문자열로 변환
     * 예: 10000 -> "10,000"
     */
    fun formatPrice(price: Int): String {
        return String.format("%,d", price)
    }
    
    /**
     * 콤마가 포함된 문자열 가격을 정수로 변환
     * 예: "10,000" -> 10000
     */
    fun parsePrice(priceString: String): Int {
        return priceString.replace(",", "").toInt()
    }
} 