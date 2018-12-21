package com.mvwchina.funcation.basicauth;

import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@SpringBootApplication
public class BasicAuthVertxApplication {

    @Resource
    private StaticServer staticServer;

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthVertxApplication.class, args);
    }

    @PostConstruct
    public void deploy() {
        Vertx.vertx().deployVerticle(staticServer);
    }
}
