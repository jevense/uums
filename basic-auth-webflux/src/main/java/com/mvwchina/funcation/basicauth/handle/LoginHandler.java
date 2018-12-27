package com.mvwchina.funcation.basicauth.handle;

import com.google.common.collect.Lists;
import com.mvwchina.enumeration.DeviceEnum;
import com.mvwchina.funcation.basicauth.Define;
import com.mvwchina.funcation.basicauth.service.LoginService;
import com.mvwchina.util.MD5;
import com.mvwchina.util.URLDecoder;
import com.mvwchina.vo.LoginVO;
import com.mvwchina.vo.TokenVO;
import com.mvwchina.vo.ValidateVO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.mvwchina.funcation.basicauth.WebRouter.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/18 下午2:50
 */
@Component
public class LoginHandler {

    @Value("classpath:/public/index.html")
    private Resource indexPage;

    private final Define define;

    private final LoginService loginService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public LoginHandler(Define define,
                        LoginService loginService,
                        RedisTemplate<String, Object> redisTemplate) {
        this.define = define;
        this.loginService = loginService;
        this.redisTemplate = redisTemplate;
    }

    private ServerResponse.BodyBuilder getBodyBuilder() {
        return ok().contentType(MediaType.valueOf("text/html;charset=utf-8"));
    }

    /**
     * WARNING!
     * ServerResponse接口的默认实现DefaultServerResponseBuilder，
     * 其render方法在传递过程中丢弃cookie,故在header中设置cookie
     * <p>
     * <code>
     * return new DefaultRenderingResponseBuilder(name)
     * .headers(this.headers)
     * .status(this.statusCode)
     * .modelAttributes(modelAttributes)
     * .build()
     * .map(renderingResponse -> renderingResponse);
     * <code/>
     *
     * @author lujiewen
     * @since 2018/12/27
     */
    private ServerResponse.BodyBuilder getCookieBodyBuilder(TokenVO tokenVO) {

        return getBodyBuilder()
                .header("Set-Cookie", ResponseCookie
                        .from(X_MVW_USERID, tokenVO.getUserID())
                        .maxAge(define.getTokenAlive() * 24 * 3600)
                        .build().toString())
                .header("Set-Cookie", ResponseCookie
                        .from(ACCESS_KEY, tokenVO.getToken())
                        .maxAge(define.getTokenAlive() * 24 * 3600)
                        .build().toString())
                .header("Set-Cookie", ResponseCookie
                        .from(DEVICE_TYPE, tokenVO.getDevice())
                        .maxAge(define.getTokenAlive() * 24 * 3600)
                        .build().toString());
    }

    public ServerRequest auth(ServerRequest request) {
        request.formData().subscribe(map -> {
            val tokenVO = loginService
                    .findByPhone(map.getFirst("account"))
                    .map(acc -> {
                        val loginVO = LoginVO.builder()
                                .account(map.getFirst("account"))
                                .password(map.getFirst("password"))
                                .build();

                        if (!loginService.auth(loginVO, acc)) {
                            return TokenVO.builder().status(false).build();
                        }

                        val device = Optional.ofNullable(map.getFirst("device"))
                                .orElse(DeviceEnum.PC.name());

                        //3天后过期
                        Date expireDate = Date.from(LocalDateTime.now()
                                .plusDays(define.getTokenAlive())
                                .atZone(ZoneId.systemDefault())
                                .toInstant());
                        String token = MD5.encode(expireDate.toString(), device);

                        /* set redis start */
                        //[token,deviceid,appid,expire]
                        List tokenResult = Lists.newArrayList(token, DeviceEnum.valueOf(device), acc.getUseId(), expireDate);
                        redisTemplate.opsForHash().put(acc.getUseId(), device, tokenResult);

                        return TokenVO.builder()
                                .status(true)
                                .userID(acc.getUseId())
                                .token(token)
                                .device(device)
                                .expiresIn(define.getTokenAlive() * 24 * 3600)
                                .build();

                    }).orElse(TokenVO.builder().status(false).build());

            request.attributes().put(MVW_VALIDATE_TOKEN, tokenVO);
        });
        return request;
    }

    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        val httpHeaders = request.headers().asHttpHeaders();
        val userIDCookie = request.cookies().getFirst(X_MVW_USERID);
        val accessKeyCookie = request.cookies().getFirst(ACCESS_KEY);
        val deviceTypeCookie = request.cookies().getFirst(DEVICE_TYPE);

        val userID = Objects.nonNull(userIDCookie) ? userIDCookie.getValue() : httpHeaders.getFirst(X_MVW_USERID);
        val accessKey = Objects.nonNull(accessKeyCookie) ? accessKeyCookie.getValue() : httpHeaders.getFirst(ACCESS_KEY);
        val device = Optional.ofNullable(Objects.nonNull(deviceTypeCookie) ? deviceTypeCookie.getValue() : httpHeaders.getFirst(DEVICE_TYPE)).orElse("PC");

        boolean status = Objects.nonNull(userID) &&
                redisTemplate.hasKey(userID) &&
                redisTemplate.boundHashOps(userID).hasKey(device) &&
                ((ArrayList) redisTemplate.boundHashOps(userID).get(device)).get(0).equals(accessKey) &&
                new Date().getTime() < (Long) ((ArrayList) redisTemplate.boundHashOps(userID).get(device)).get(3);

        if (request.path().matches("/validate")) {
            val nextRequest = ServerRequest.from(request);
            nextRequest.attribute("x-mvw-validate", status);
            return next.handle(nextRequest.build());
        }

        if (status) return next.handle(request);

        val uriComponents = UriComponentsBuilder.fromPath("/login")
                .queryParam("redirect_uri", "{redirect_uri}")
                .build(request.uri().toString());

        return ServerResponse.temporaryRedirect(uriComponents).build();
    }

    public Mono<ServerResponse> indexPage(ServerRequest serverRequest) {
        return getBodyBuilder().syncBody(indexPage);
    }

    public Mono<ServerResponse> loginPage(ServerRequest serverRequest) {
        return getBodyBuilder().render("login");
    }

    public Mono<ServerResponse> homePage(ServerRequest serverRequest) {
        return getBodyBuilder().render("home");
    }

    /**
     * @apiDefine ServerError
     * @apiError (Error 5xx) 503 服务不可用
     */
    /**
     * @api {post} /login 登录
     * @apiName validate
     * @apiVersion 0.0.1
     * @apiGroup login
     * @apiExample {curl} 接口示例:
     * curl -i https://api.mvwchina.com/basic-auth/login
     * @apiHeader {String} X-MVW-ENV 请求头必须携带字段X-MVW-ENV
     * @apiHeaderExample {json} 头部示例:
     * {
     * "X-MVW-ENV": {
     * "appID": "应用ID，应用在公司内部的编号",
     * "appVersion": "应用版本",
     * "deviceType": "设备类型，WEB/Android/IOS",
     * "deviceModel": "设备型号",
     * "deviceID": "设备ID",
     * "pushToken": "发送PUSH的Token，对移动设备有意义",
     * "ip": "IP地址，用来做一些异常检查和控制",
     * "userAgent": "浏览器User-Agent",
     * "osType": "设备操作系统类型",
     * "osVersion": "设备操作系统版本",
     * "market": "市场推广渠道"
     * }
     * }
     * @apiParam {String="ACCOUNT_AND_PASSWORD","PHONE_AND_PASSWORD","PHONE_AND_CODE"} type 验证类型：
     * <br>ACCOUNT_AND_PASSWORD——帐号密码，<br>PHONE_AND_PASSWORD——手机号和密码，<br>PHONE_AND_CODE——手机号和验证码<br>
     * @apiParam {String{..18}} account 登录帐号/手机号
     * @apiParam {String} password 密码/手机验证码
     * @apiParamExample {json} 请求示例:
     * {
     * "type": "ACCOUNT_AND_PASSWORD",
     * "account": "1234568999",
     * "password": "*(@#@!!)!)"
     * }
     * @apiSuccess {Boolean} status 登录是否成功
     * @apiSuccess {String} userID 用户ID
     * @apiSuccess {String} token 用户令牌
     * @apiSuccess {String} errorMessage 登录失败原因
     * @apiSuccessExample {json} 成功响应示例:
     * HTTP/1.1 200 OK
     * {
     * "status": true,
     * "userID": "98231423423453efa23es9232"，
     * "token": "BWKSPPP+WSLQOWMRNBSIIWW"
     * }
     * @apiSuccessExample {json} 失败响应示例:
     * HTTP/1.1 200 OK
     * {
     * "status": false,
     * "errorMessage": "用户名密码不匹配"
     * }
     * @apiError (Error 4xx) 404 帐号不存在
     * @apiUse ServerError
     */
    public Mono<ServerResponse> login(ServerRequest request) {

        return request.attribute(MVW_VALIDATE_TOKEN)
                .map(tokenVO -> {
                    val token = (TokenVO) tokenVO;
                    if (!token.isStatus()) return status(HttpStatus.UNAUTHORIZED).render("login");
                    val render = request
                            .queryParam("redirect_uri")
                            .map(redirectUri -> getCookieBodyBuilder(token)
                                    .render("redirect:" + UriComponentsBuilder
                                            .fromHttpUrl(URLDecoder.decode(redirectUri))
                                            .build()
                                            .toUri())
                            )
                            .orElseGet(() -> getCookieBodyBuilder(token).render("redirect:home"));

                    return render;
                })
                .orElseGet(() -> status(HttpStatus.UNAUTHORIZED).render("login"));
    }

    public Mono<ServerResponse> token(ServerRequest request) {
        return request
                .attribute(MVW_VALIDATE_TOKEN)
                .map(tokenVO -> ok().body(Mono.just((TokenVO) tokenVO), TokenVO.class))
                .orElseGet(() -> ok()
                        .body(Mono.just(TokenVO
                                .builder()
                                .status(false)
                                .build()), TokenVO.class));

    }

    public Mono<ServerResponse> validate(ServerRequest serverRequest) {
        val status = (boolean) serverRequest.attribute("x-mvw-validate").orElse(false);
        return ok().body(Mono.just(new ValidateVO(status)), ValidateVO.class);
    }
}

