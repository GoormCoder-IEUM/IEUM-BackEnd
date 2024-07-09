package com.goormcoder.ieum.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "IEUM API 명세서",
                description = "구름톤 트레이닝 기업연계 프로젝트",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){

        Components components = new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                );

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement);
    }

}