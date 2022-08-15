package com.example.rocksdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SwaggerConfig
 * @Author kebukeyi
 * @Date 2022/8/15 11:30
 * @Description
 * @Version 1.0.0
 */
@Configuration // 标明是配置类
@EnableSwagger2 //开启swagger功能
public class SwaggerConfig {


    private static final String headerKey = "token"; //header参数的key

    @Bean
    public Docket createRestApi() {
        // 添加header参数headerKey
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameterList = new ArrayList<>();
        parameterBuilder.name(headerKey).description(headerKey)
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        parameterList.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.rocksdb.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameterList)
                .apiInfo(new ApiInfoBuilder()
                        .title("spring-boot-util")
                        .description("")
                        .version("v1.0")
                        .contact(new Contact("", "", ""))
                        .license("")
                        .licenseUrl("")
                        .build());
    }
}
