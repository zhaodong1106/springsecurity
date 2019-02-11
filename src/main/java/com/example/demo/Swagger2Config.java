package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

/**
 * Created by T011689 on 2018/11/27.
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    private static  final  String  accessTokenUri ="http://localhost:8080/oauth/token";
    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations"),
                new AuthorizationScope("foo", "Access foo API") };
        return scopes;
    }
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference("spring_oauth", scopes())))
                .forPaths(PathSelectors.regex("/foos.*"))
                .build();
    }
    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint("/token", "oauthtoken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint( "/authorize", "SampleClientId", "SampleClientId"))
                .build();

        SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }
    @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration("SampleClientId",
                "secret",
                "","","", ApiKeyVehicle.HEADER, "", "");
    }
    @Value(value = "${swagger.swaggerSwitch}")
     private boolean swaggerSwitch;
    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()));
        if(swaggerSwitch){
            docket.enable(true);
        }else {
            docket.enable(false);
        }
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("demo系统API")
                .description("demo系统API")
                .termsOfServiceUrl("http://127.0.0.1:8081/")
                .contact(new Contact("zhaodong",null,null))
                .version("1.0")
                .build();
    }

}
