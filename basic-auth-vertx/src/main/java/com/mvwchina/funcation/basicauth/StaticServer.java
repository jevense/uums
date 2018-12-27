package com.mvwchina.funcation.basicauth;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterType;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/2 上午10:50
 */
@Component
public class StaticServer extends AbstractVerticle {

    @Resource
    private AppConfiguration configuration;

    @Override
    public void start() {
        Router router = Router.router(vertx);

        HTTPRequestValidationHandler validationHandler = HTTPRequestValidationHandler.create()
                .addQueryParam("parameterName", ParameterType.INT, true)
                .addFormParamWithPattern("formParameterName", "a{4}", true)
                .addPathParam("pathParam", ParameterType.FLOAT);

        TemplateEngine engine = ThymeleafTemplateEngine.create();
        TemplateHandler handler = TemplateHandler.create(engine);

        router.route()
                .handler(StaticHandler.create())
                .handler(BodyHandler.create())
                .handler(handler)
                .handler(validationHandler)
                .handler(CookieHandler.create())
                .handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        router.route().handler(routingContext -> {
            // 从请求上下文获取session
            Session session = routingContext.session();
            int count = Optional.ofNullable((Integer) session.get("count")).orElse(0);
            session.put("count", ++count);

            Optional<Cookie> cookie = Optional.ofNullable(routingContext.getCookie("testCookie"));

            String tar = "0";
            if (cookie.isPresent() && cookie.get().getValue().matches("\\d+")) {
                tar = String.valueOf(Integer.valueOf(cookie.get().getValue()) + 1);
            }
            routingContext.addCookie(Cookie.cookie("testCookie", tar));


            routingContext.response()
                    .putHeader("content-type", "text/html")
//                    .end("total visit count:" + session.get("count"))
                    .end("total visit count:" + tar);
        });

        router.post("/login").handler(this::handleLogin);
        router.post("/post/:param1/:param2").handler(this::handlePost);
        router.get("/get/:param1/:param2").handler(this::handleGet);

        vertx.createHttpServer().requestHandler(router::accept).listen(configuration.httpPort());
    }

    private void handleLogin(RoutingContext context) {
        String param1 = context.queryParam("redirect_uri").get(0);
        System.out.println(param1);
        context.response().end("OK");
    }

    // 处理post请求的handler
    private void handlePost(RoutingContext context) {
        // 从上下文获取请求参数，类似于从httprequest中获取parameter一样
        String param1 = context.request().getParam("param1");
        String param2 = context.request().getParam("param2");

        if (StringUtils.isEmpty(param1) || StringUtils.isEmpty(param2)) {
            // 如果参数空，交由httpserver提供默认的400错误界面
            context.response().setStatusCode(400).end();
        }

        JsonObject obj = new JsonObject();
        obj.put("method", "post").put("param1", param1).put("param2", param2);

        // 申明response类型为json格式，结束response并且输出json字符串
        context.response().putHeader("content-type", "application/json")
                .end(obj.encodePrettily());
    }

    // 逻辑同post方法
    private void handleGet(RoutingContext context) {
        String param1 = context.request().getParam("param1");
        String param2 = context.request().getParam("param2");

        if (StringUtils.isEmpty(param1) || StringUtils.isEmpty(param2)) {
            context.response().setStatusCode(400).end();
        }
        JsonObject obj = new JsonObject();
        obj.put("method", "get").put("param1", param1).put("param2", param2);

        context.response().putHeader("content-type", "application/json")
                .end(obj.encodePrettily());
    }

}