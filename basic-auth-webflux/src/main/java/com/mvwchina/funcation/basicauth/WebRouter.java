package com.mvwchina.funcation.basicauth;

import com.mvwchina.funcation.basicauth.handle.CityHandler;
import com.mvwchina.funcation.basicauth.handle.LoginHandler;
import com.mvwchina.funcation.basicauth.handle.PersonHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
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

    public static final String MVW_VALIDATE_TOKEN = "mvw-validate-token";
    public static final String X_MVW_USERID = "X-MVW-userID";
    public static final String ACCESS_KEY = "access-key";
    public static final String DEVICE_TYPE = "device-type";

    private final PersonHandler personHandler;

    private final LoginHandler loginHandler;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public WebRouter(PersonHandler personHandler,
                     LoginHandler loginHandler,
                     RedisTemplate<String, Object> redisTemplate) {
        this.personHandler = personHandler;
        this.loginHandler = loginHandler;
        this.redisTemplate = redisTemplate;
    }

    /**
     * WARNING! ServerRequest.from(request).build() 之后
     * attributes(xxx,xxx)增加的属性不会传递
     *
     * @author lujiewen
     * @since 2018/12/27
     */
    @Bean
    public RouterFunction<ServerResponse> routeLogin() {

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

    @Bean
    public RouterFunction<ServerResponse> routeBusiness(CityHandler cityHandler) {
        return route()
                .GET("/hello", cityHandler::helloCity)
                .GET("/home", loginHandler::homePage)
                .GET("/validate", loginHandler::validate)
                .filter(loginHandler::filter)
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
