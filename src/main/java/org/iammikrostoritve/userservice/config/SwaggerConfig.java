package org.iammikrostoritve.userservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("User Service")
                        .version("1.0.0")
                        .description("Spring documentation for User Service.")
        );
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/docs", "/swagger-ui.html");
    }
}
