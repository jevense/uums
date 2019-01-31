package com.mvwchina.funcation.basicauth;

import com.mvwchina.enumeration.DeviceEnum;
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

//        if (servletRequest.getCookies() != null) {
//            for (Cookie cookie : servletRequest.getCookies()) {
//                log.debug(cookie.getName() + "->" + cookie.getValue() + "->" + cookie.getDomain());
//            }
//        }

        Map<String, String> authInfo = Arrays
                .stream(Optional
                        .ofNullable(servletRequest.getCookies())
                        .orElse(new Cookie[]{}))
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue, (val1, val2) -> val2));

        String userID = Optional
                .ofNullable(authInfo.get(PkgConst.X_MVW_USER_ID))
                .orElse(servletRequest.getHeader(PkgConst.X_MVW_USER_ID));

        String accessKey = Optional
                .ofNullable(authInfo.get(PkgConst.ACCESS_KEY))
                .orElse(servletRequest.getHeader(PkgConst.ACCESS_KEY));

        String device = Optional
                .ofNullable(authInfo.get(PkgConst.DEVICE_TYPE))
                .orElse(Optional
                        .ofNullable(servletRequest.getHeader(PkgConst.DEVICE_TYPE))
                        .orElse(DeviceEnum.PC.name()));


        boolean status = Optional.ofNullable(userID)
                .map(redisTemplate::<String, List>boundHashOps)
                .map(hash -> hash.get(device))
                .filter(list -> list.get(0).equals(accessKey))
                .map(list -> (long) list.get(3) > new Date().getTime())
                .orElse(false);

        if (servletRequest.getServletPath().startsWith("/validate")) {
            servletRequest.setAttribute(PkgConst.X_MVW_VALIDATE, status);
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
