package com.example.keshe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KesheApplication {
    public static void main(String[] args) {
        SpringApplication.run(KesheApplication.class, args);
    }
}