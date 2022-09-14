package com.minzheng.blog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spi.DocumentationType
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.Contact

/**
 * Knife4j配置类
 *
 * @author c
 */
@Configuration
@EnableSwagger2WebMvc
class Knife4jConfig {
    @Bean
    fun createRestApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .protocols(setOf("https"))
            .host("https://www.talkxj.com")
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.minzheng.blog.controller"))
            .paths(PathSelectors.any())
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("博客api文档")
            .description("springboot+vue开发的博客项目")
            .contact(Contact("风丶宇", "https://github.com/X1192176811", "1192176811@qq.com"))
            .termsOfServiceUrl("https://www.talkxj.com/api")
            .version("1.0")
            .build()
    }
}