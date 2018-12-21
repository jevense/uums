//package com.mvwchina.funcation.basicauth.filter;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//
///**
// * Name:
// * Description:
// * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
// * Company: 北京医视时代科技发展有限公司
// *
// * @author lujiewen
// * @version 1.0
// * @since 2018/12/2 下午9:25
// */
////@Component
////public class TokenWebFilter implements WebFilter {
////
////    @Override
////    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
////        URI uri = exchange.getRequest().getURI();
////
////        //登录接口放行
////        if (uri.getPath().equals("/login")) {
////            return chain.filter(exchange);
////        }
////
//////        String actualScheme = uri.getScheme();
//////        String actualHost = uri.getHost();
////
//////        System.out.println(actualHost);
//////        System.out.println(exchange.getRequest().getHeaders());
//////        System.out.println(exchange.getRequest().getCookies());
////
//////        ServerHttpRequest serverHttpRequest =
//////        if(){
//////
//////        }
////
//////        if(exchange.getRequest()){
//////
//////        }
////
//////        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
////
////        return chain.filter(exchange);
////    }
////}
