package com.devtaco.fashion.infrastructure.persistence.entity

import com.devtaco.fashion.domain.model.ProductStatus
import jakarta.persistence.*

/**
 * 의류(Cloth) 엔티티
 * - 카테고리, 브랜드, 가격, 상태값을 가짐
 * 
 * 실제 운영이라면
 * 상품코드, 상품명, 상품설명, 상품이미지, 재고량, 적용가능한 할인코드, 상품 옵션 등이 추가될 수 있으나,
 * 과제 목적이고 모든 필드를 추가하지 않아도 되기 때문에 생략함
 */
@Entity
@Table(
    name = "product",
    indexes = [
        Index(
            name = "idx_status_brand_category_price_id",
            columnList = "status, brand_id, category_id, price, id"
        )
    ]
)
class ProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: CategoryEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: BrandEntity,

    @Column(nullable = false)
    val price: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ProductStatus,


    @Column(nullable = false, name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @Column(nullable = false, name = "updated_at")
    var updatedAt: Long = System.currentTimeMillis(),
) {

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = System.currentTimeMillis()
    }
}