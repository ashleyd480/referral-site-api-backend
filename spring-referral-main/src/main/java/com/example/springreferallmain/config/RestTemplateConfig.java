package com.example.springreferallmain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}


// @Configuration tells Spring to look inside this class at application start and create bean of the annotated methods, so we can then autowire them
// @Bean is a method-level annotation that allows us to create a bean of that specific method
// RestTemplate is a class provided by Spring Framework that allows us to make Rest calls to other web services (in this case Spring ReferAll Ma