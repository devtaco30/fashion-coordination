package com.devtaco.fashion.global.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("백엔드 엔지니어 기술 과제")
                    .description("패션 상품 관리 시스템을 구현합니다.")
                    .version("1.0.0")
            )
            
    }
} 