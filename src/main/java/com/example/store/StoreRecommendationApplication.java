package com.example.store;

import io.jaegertracing.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication
public class StoreRecommendationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreRecommendationApplication.class, args);
    }

//    @Bean
//    public io.opentracing.Tracer tracer() {
//        return Configuration.fromEnv().getTracer();
//    }

}
