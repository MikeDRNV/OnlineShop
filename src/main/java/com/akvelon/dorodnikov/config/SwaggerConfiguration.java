package com.akvelon.dorodnikov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Provides interactively view the specification RESTful API and send requests.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    /**
     * Docket Bean for configuring Swager2 for Spring Boot application.
     *
     * @return Instance of ApiSelectorBuilder, which provides a way to control the endpoints exposed by Swagger.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    /**
     * @return Api information.
     */
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title("online shop")
                .build();
    }
}