package com.autoyol.field.progress.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableFeignClients(basePackages = "com.autoyol")
@EnableHystrix
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.autoyol")
public class FieldProgressApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FieldProgressApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FieldProgressApplication.class);
    }
}