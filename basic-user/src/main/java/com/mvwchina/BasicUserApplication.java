package com.mvwchina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BasicUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicUserApplication.class, args);
    }
}
