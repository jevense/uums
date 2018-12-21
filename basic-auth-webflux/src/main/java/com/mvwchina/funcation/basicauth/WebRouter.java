package com.mvwchina.funcation.basicauth;

import com.mvwchina.funcation.basicauth.handle.CityHandler;
import com.mvwchina.funcation.basicauth.handle.LoginHandler;
import com.mvwchina.funcation.basicauth.handle.PersonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/18 上午11:35
 */
@Configuration
public class WebRouter {

    private final PersonHandler personHandler;

    private final LoginHandler loginHandler;

    @Autowired
    public WebRouter(PersonHandler personHandler, LoginHandler loginHandler) {
        this.personHandler = personHandler;
        this.loginHandler = loginHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> routeLogin() {
        return route().POST("/login", contentType(MediaType.APPLICATION_JSON), loginHandler::token).build();
    }

    @Bean
    public RouterFunction<ServerResponse> routeCity(CityHandler cityHandler) {
        return route(
                GET("/hello").and(accept(MediaType.TEXT_PLAIN)), cityHandler::helloCity
        );
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionA() {
        return route().path("/person", builder -> builder.nest(accept(APPLICATION_JSON), build -> build
                .GET("/{id}", personHandler::getPerson)
                .GET("", personHandler::listPeople)
                .before(request -> ServerRequest.from(request)
                        .header("X-RequestHeader", "Value")
                        .build()))
                .POST("/", personHandler::createPerson))
                .build();
    }
}
