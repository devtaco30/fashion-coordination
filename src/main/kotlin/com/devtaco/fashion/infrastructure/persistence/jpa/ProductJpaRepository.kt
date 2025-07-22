package com.devtaco.fashion.infrastructure.persistence.jpa

import com.devtaco.fashion.infrastructure.persistence.entity.ProductEntity
import com.devtaco.fashion.infrastructure.persistence.entity.CategoryEntity
import com.devtaco.fashion.domain.model.Product
import com.devtaco.fashion.domain.model.ProductStatus
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * JPA용 ProductEntity 저장소
 */
interface ProductJpaRepository : JpaRepository<ProductEntity, Long?> {
    @Query(
    """
        SELECT *
        FROM (
            SELECT *,
                ROW_NUMBER() OVER (
                    PARTITION BY category_id
                    ORDER BY price ASC, id ASC
                ) as rn
            FROM product
            WHERE status = 'NORMAL'
        ) t
        WHERE rn = 1
        """,
        nativeQuery = true
    )
    fun findMinPriceProductsByAllCategories(): List<ProductEntity>

    /**
     * 아래 쿼리로 사용할 시, DB 만을 사용해서 한 번에, 
     * [ProductQueryService.getLowestPriceCoordiSetByBrand] 를 처리할 수 있다.
     * 
     * 하지만, 쿼리 자체가 복잡할 뿐더러, 브랜드, 카테고리, 상품 자체의 데이터가 많아지면
     * 그만큼 쿼리 실행 시간도 길어질 것이고 DB 에 부하도 커질 것이다.
     * 
     * """
     * WITH min_brand AS (
            SELECT brand_id
            FROM (
                SELECT brand_id, SUM(min_price) AS total_min_price
                FROM (
                    SELECT brand_id, category_id, MIN(price) AS min_price
                    FROM product
                    WHERE status = 'NORMAL'
                    GROUP BY brand_id, category_id
                ) category_min_prices
                GROUP BY brand_id
                HAVING COUNT(DISTINCT category_id) = (SELECT COUNT(*) FROM category)
                ORDER BY total_min_price ASC, brand_id ASC
                LIMIT 1
            )
        )
        SELECT *
        FROM product p
        WHERE p.status = 'NORMAL'
        AND p.brand_id = (SELECT brand_id FROM min_brand)
        AND p.price = (
            SELECT MIN(p3.price)
            FROM product p3
            WHERE p3.brand_id = p.brand_id
            AND p3.category_id = p.category_id
            AND p3.status = 'NORMAL'
        )
        ORDER BY p.category_id ASC
     * """
     * 
     * 따라서, 아래 쿼리를 사용해서 브랜드별, 카테고리별 최저가 상품을 조회하고,
     * 그 이후의 부분은 서버에서 처리하도록 한다.
     * 
     */
    @Query("""
        SELECT id, brand_id, category_id, price, status, created_at, updated_at
        FROM (
            SELECT *,
                ROW_NUMBER() OVER (
                    PARTITION BY brand_id, category_id
                    ORDER BY price ASC, id ASC
                ) AS rn
            FROM product
            WHERE status = 'NORMAL'
        ) t
        WHERE rn = 1
    """, nativeQuery = true)
    fun findBrandCategoryMinProducts(): List<ProductEntity>


    @Query("""
        SELECT p FROM ProductEntity p 
        WHERE p.brand.id = :brandId 
        AND p.category.id = :categoryId 
        AND p.status = 'NORMAL'
        ORDER BY p.price ASC
    """)
    fun findMinPriceByBrandAndCategory(
        @Param("brandId") brandId: Long, 
        @Param("categoryId") categoryId: Long
    ): Optional<ProductEntity>
    
    @Query("""
        SELECT p FROM ProductEntity p 
        WHERE p.category.id = :categoryId 
        AND p.status = 'NORMAL'
        ORDER BY p.price ASC
    """)
    fun findAllByCategory(@Param("categoryId") categoryId: Long): List<ProductEntity>
    
    fun findFirstByCategoryIdAndStatusOrderByPriceAscIdAsc(categoryId: Long, status: ProductStatus): Optional<ProductEntity>
    
    fun findFirstByCategoryIdAndStatusOrderByPriceDescIdAsc(categoryId: Long, status: ProductStatus): Optional<ProductEntity>
    
    fun existsByCategoryId(categoryId: Long): Boolean
}