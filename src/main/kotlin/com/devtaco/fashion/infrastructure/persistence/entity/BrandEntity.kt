package com.devtaco.fashion.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "brand")
class BrandEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, unique = true)
    val name: String,

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