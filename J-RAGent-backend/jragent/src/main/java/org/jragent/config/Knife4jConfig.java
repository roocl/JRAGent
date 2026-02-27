package org.jragent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI jrAgentOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("J-RAGent 接口文档")
                        .description("引入RAG技术的AI助手")
                        .version("v1.0.0")
                        .contact(new Contact().name("roocl")));
    }

    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/api/**", "/sse/**")
                .build();
    }
}
