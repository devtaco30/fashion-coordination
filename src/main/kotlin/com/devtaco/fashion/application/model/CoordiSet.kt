package com.devtaco.fashion.application.model

/**
 * 통합 코디세트 제안 클래스
 * 
 * 요구 사항에 따라, 
 * [LowestBrandCoordiSet], [LowestCoordiSet] 을 따로 만들었습니다.
 * 
 * 하지만, 유사한 형태의 데이터 세트로 판단하여 하나의 클래스로 합쳐, 
 * 요구 사항에 따라 응답 형태를 변경하는 방식을 제안하기 위해 이 클래스를 만들어 두었습니다.
 * 
 * 현재는 제안용으로만 사용되며, 실제 API에서는 사용되지 않습니다.
 */
data class CoordiSet(
    val products: List<CoordiSetItem>,
    val totalPrice: Int,
)

/**
 * 코디세트 아이템 정보
 * 
 * 통합 코디세트에서 사용되는 개별 상품 정보를 담는 클래스입니다.
 * 상품 ID, 브랜드명, 카테고리명, 가격 정보를 포함합니다.
 */
data class CoordiSetItem(
    val id: Long,
    val brand: String,
    val category: String,
    val price: Int,
)