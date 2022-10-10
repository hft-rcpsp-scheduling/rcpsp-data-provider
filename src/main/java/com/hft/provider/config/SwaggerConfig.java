package com.hft.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    /**
     * <h2>Swagger Configuration ({@link DocumentationType#SWAGGER_2})</h2>
     * <ol>
     *     <li>Add custom {@link SwaggerConfig#apiInfo()}.</li>
     *     <li>Select all controller from the base package with all paths.</li>
     * </ol>
     *
     * @return {@link Docket} for the swagger page.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * <h2>General Api Info Configuration</h2>
     * This {@link ApiInfo} has no functional usage. It displays customized information on top of the page.
     *
     * @return customized {@link ApiInfo} for api description.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "HFT RCPSP Data Provider",
                "Educational Project",
                "0.0.1",
                "https://www.apache.org/licenses/LICENSE-2.0.html",
                new Contact("Felix Steinke", "https://github.com/felixsteinke", "No direct mail contact."),
                "No License yet.",
                "https://www.apache.org/licenses/LICENSE-2.0.html",
                Collections.emptyList());
    }
}
