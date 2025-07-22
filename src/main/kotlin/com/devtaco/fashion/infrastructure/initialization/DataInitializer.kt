package com.devtaco.fashion.infrastructure.initialization

import com.devtaco.fashion.domain.model.*
import com.devtaco.fashion.domain.port.BrandRepository
import com.devtaco.fashion.domain.port.ProductRepository
import com.devtaco.fashion.domain.port.CategoryRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service
import jakarta.annotation.PostConstruct

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@Service
class DataInitializer(
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // 1. 카테고리 초기화
        initializeCategories()
        
        // 2. 브랜드 및 상품 초기화 (기본 데이터)
        initializeProducts("/data/price-table.json")
        
        // 3. 추가 상품 데이터 초기화
        initializeProducts("/data/price-table-additional.json")

        System.out.println("============== DataInitializer completed ==============")
    }
    
    private fun initializeCategories() {
        val mapper = jacksonObjectMapper()
        val inputStream = javaClass.getResourceAsStream("/data/categories.json")
            ?: throw IllegalStateException("categories.json not found")
        
        data class CategoryData(val name: String)
        val categories: List<CategoryData> = mapper.readValue(inputStream)
        val categoryList = categories.map { categoryData ->
            Category(id = 0L, name = categoryData.name)
        }
        categoryRepository.saveAll(categoryList)
    }
    
    private fun initializeProducts(filePath: String) {
        val mapper = jacksonObjectMapper()
        val inputStream = javaClass.getResourceAsStream(filePath)
            ?: throw IllegalStateException("$filePath not found")
        val priceTable: Map<String, Map<String, Int>> = mapper.readValue(inputStream)

        priceTable.forEach { (brandName, categoryPriceMap) ->
            // 브랜드가 이미 존재하는지 확인하고, 없으면 생성
            val brand = brandRepository.findByName(brandName) 
                ?: brandRepository.save(Brand(id = 0L, name = brandName))
            categoryPriceMap.forEach { (categoryName, price) ->
                val category = categoryRepository.findByName(categoryName)
                    ?: throw IllegalArgumentException("존재하지 않는 카테고리명: $categoryName")
                
                val product = Product(
                    id = 0L,
                    category = category,
                    brand = brand,
                    price = price,
                    status = ProductStatus.NORMAL
                )
                productRepository.save(product)
            }
        }
    }
} 