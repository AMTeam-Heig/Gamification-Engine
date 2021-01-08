package ch.heig.amt.gamification.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-19T13:42:22.997Z[GMT]")
@Configuration
public class SwaggerDocumentationConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.OAS_30)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("ch.heig.amt.gamification.api"))
                    .build()
                .directModelSubstitute(org.threeten.bp.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.threeten.bp.OffsetDateTime.class, java.util.Date.class)
                .securitySchemes(Arrays.asList(apiKey()))
                .apiInfo(apiInfo());
    }
    @Bean
    public OpenAPI configure() {
        return new OpenAPI()
            .info(new Info()
                .title("Gamification API")
                .description("An API to add a gamification process to a simple site with Swagger and Spring Boot")
                .termsOfService("")
                .version("0.1.0")
                .license(new License()
                    .name("")
                    .url("http://unlicense.org")));
    }

    private ApiKey apiKey() {
        return new ApiKey("X-API-KEY", "Authorization", "header");
    }

}
