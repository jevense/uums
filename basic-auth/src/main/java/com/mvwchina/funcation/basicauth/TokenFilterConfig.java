package com.mvwchina.funcation.basicauth;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/21 下午1:02
 */
@Slf4j
@Order(1)
@WebFilter(filterName = "tokenFilter", urlPatterns = "/*")
@Component
public class TokenFilterConfig implements Filter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        String servletPath = servletRequest.getServletPath();

        if (servletPath.matches("/") ||
                servletPath.matches("/static/.*") ||
                servletPath.matches("/(login|(index\\.html))")) {
            chain.doFilter(request, response);
            return;
        }

        Map<String, String> authInfo = Arrays
                .stream(Optional
                        .ofNullable(servletRequest.getCookies())
                        .orElse(new Cookie[]{}))
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));

        String userID = Optional
                .ofNullable(authInfo.get("X-MVW-userID"))
                .orElse(servletRequest.getHeader("X-MVW-userID"));

        String accessKey = Optional
                .ofNullable(authInfo.get("access-key"))
                .orElse(servletRequest.getHeader("access-key"));

        String device = Optional
                .ofNullable(authInfo.get("device-type"))
                .orElse(Optional
                        .ofNullable(servletRequest.getHeader("device-type"))
                        .orElse("PC"));


        boolean status = Objects.nonNull(userID) &&
                redisTemplate.hasKey(userID) &&
                redisTemplate.boundHashOps(userID).hasKey(device) &&
                ((ArrayList) redisTemplate.boundHashOps(userID).get(device)).get(0).equals(accessKey) &&
                new Date().getTime() < (Long) ((ArrayList) redisTemplate.boundHashOps(userID).get(device)).get(3);

        if (servletRequest.getServletPath().matches("/validate")) {
            servletRequest.setAttribute("x-mvw-validate", status);
            chain.doFilter(request, response);
            return;
        }

        if (status) {
            chain.doFilter(request, response);
            return;
        }

        val redirectUri = servletRequest.getRequestURL();
        if (Objects.nonNull(servletRequest.getQueryString())) {
            redirectUri.append("?").append(servletRequest.getQueryString());
        }

        val uriComponents = UriComponentsBuilder.fromPath("/login")
                .queryParam("redirect_uri", "{redirect_uri}")
                .build(redirectUri.toString());

        servletResponse.sendRedirect(uriComponents.toString());
    }
}
