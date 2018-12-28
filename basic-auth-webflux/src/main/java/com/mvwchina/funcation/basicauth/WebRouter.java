package com.mvwchina.funcation.basicauth;

import com.mvwchina.funcation.basicauth.handle.LoginHandler;
import com.mvwchina.funcation.basicauth.handle.PersonHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
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
@Slf4j
@Configuration
public class WebRouter {

    private final PersonHandler personHandler;

    @Autowired
    public WebRouter(PersonHandler personHandler) {
        this.personHandler = personHandler;
    }

    /**
     * WARNING! ServerRequest.from(request).build() 之后
     * attributes(xxx,xxx)增加的属性不会传递
     *
     * @author lujiewen
     * @since 2018/12/27
     */
    @Bean
    public RouterFunction<ServerResponse> routeLogin(LoginHandler loginHandler) {

        return route()
                .GET("", loginHandler::indexPage)
                .GET("/index*", loginHandler::indexPage)
                .GET("/login*", loginHandler::loginPage)
                .path("/login", build -> build
                        .POST("", contentType(MediaType.APPLICATION_JSON), loginHandler::token)
                        .POST("", loginHandler::login)
                        .before(loginHandler::auth)
                )
                .build();
    }

    /**
     * 请求页面html
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    @Bean
    public RouterFunction<ServerResponse> routePage(LoginHandler loginHandler) {
        return route()
                .filter(loginHandler::filter)
                .filter(loginHandler::redirect)
                .GET("/home", loginHandler::homePage)
                .build();
    }

    /**
     * 请求JSON格式数据
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    @Bean
    public RouterFunction<ServerResponse> routeData(LoginHandler loginHandler) {
        return route()
                .filter(loginHandler::filter)
                .GET("/validate", loginHandler::validate)
                .build();
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
