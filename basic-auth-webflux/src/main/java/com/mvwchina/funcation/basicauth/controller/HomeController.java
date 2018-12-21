package com.mvwchina.funcation.basicauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/2 下午2:08
 */
@Controller
@RequestMapping(value = "home", produces = "text/html;charset=utf-8")
public class HomeController {

    @GetMapping
    public Mono<ServerResponse> homePage() {
        return ok().render("home");
    }

}
