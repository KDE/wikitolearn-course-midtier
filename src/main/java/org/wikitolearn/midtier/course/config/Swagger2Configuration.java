package org.wikitolearn.midtier.course.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).enableUrlTemplating(true).select()
        .apis(RequestHandlerSelectors.basePackage("org.wikitolearn")).paths(PathSelectors.any()).build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo("Course MidTier API", "", "v1", "",
        new Contact("WikiToLearn", "https://www.wikitolearn.org", "info@wikitolearn.org"), "AGPLv3+",
        "https://www.gnu.org/licenses/agpl-3.0.en.html", Collections.emptyList());
  }
}
