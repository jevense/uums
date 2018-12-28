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
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mvwchina.funcation.basicauth.handle.PkgConst.*;
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
 * @apiDefine ServerError
 * @apiError (Error 5xx) 503 服务不可用
 * @since 2018/12/18 下午2:50
 */
@Slf4j
@Component
public class LoginHandler {

    @Value("classpath:/public/index.html")
    private Resource indexPage;

    private final Define define;

    private final LoginService loginService;

    private final RedisTemplate<String, Map<String, List>> redisTemplate;

    @Autowired
    public LoginHandler(Define define,
                        LoginService loginService,
                        RedisTemplate<String, Map<String, List>> redisTemplate) {
        this.define = define;
        this.loginService = loginService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 页面请求正确，放回html类型数据的builder封装
     *
     * @author lujiewen
     * @since 2018/12/28
     */
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
                        .from(MVW_USER_ID, tokenVO.getUserID())
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

    private ServerResponse.BodyBuilder clearCookieBodyBuilder() {

        return getBodyBuilder()
                .header("Set-Cookie", ResponseCookie
                        .from(MVW_USER_ID, Optional.empty().toString())
                        .maxAge(0)
                        .build().toString())
                .header("Set-Cookie", ResponseCookie
                        .from(ACCESS_KEY, Optional.empty().toString())
                        .maxAge(0)
                        .build().toString())
                .header("Set-Cookie", ResponseCookie
                        .from(DEVICE_TYPE, Optional.empty().toString())
                        .maxAge(0)
                        .build().toString());
    }

    /**
     * 登录认证过滤器
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    public ServerRequest auth(ServerRequest request) {
        request.formData().subscribe(map -> {
            val tokenVO = loginService
                    .findByPhone(map.getFirst("account"))
                    .filter(acc -> loginService.auth(LoginVO.builder()
                            .account(map.getFirst("account"))
                            .password(map.getFirst("password"))
                            .build(), acc))
                    .map(acc -> {
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
                        redisTemplate.<String, List>opsForHash().put(acc.getUseId(), device, tokenResult);

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

    /**
     * 项目页面认证过滤器
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        val httpHeaders = request.headers().asHttpHeaders();

        val userID = Optional
                .ofNullable(request.cookies().getFirst(MVW_USER_ID))
                .map(HttpCookie::getValue)
                .orElse(httpHeaders.getFirst(MVW_USER_ID));
        val accessKey = Optional
                .ofNullable(request.cookies().getFirst(ACCESS_KEY))
                .map(HttpCookie::getValue)
                .orElse(httpHeaders.getFirst(ACCESS_KEY));
        val device = Optional
                .ofNullable(request.cookies().getFirst(DEVICE_TYPE))
                .map(HttpCookie::getValue)
                .orElse(Optional.ofNullable(httpHeaders.getFirst(DEVICE_TYPE)).orElse(DeviceEnum.PC.name()));

        val status = Optional.ofNullable(userID)
                .map(redisTemplate::<String, List>boundHashOps)
                .map(hash -> hash.get(device))
                .filter(list -> list.get(0).equals(accessKey))
                .map(list -> (long) list.get(3) > new Date().getTime())
                .orElse(false);

        request.attributes().put(MVW_VALIDATE_STATUS, status);
        return next.handle(request);
    }

    /**
     * 判断前置过滤器状态，决定是否重定向到登录页面
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    public Mono<ServerResponse> redirect(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return request.attribute(MVW_VALIDATE_STATUS)
                .filter(status -> (boolean) status)
                .map(status -> next.handle(request))
                .orElse(ServerResponse
                        .temporaryRedirect(UriComponentsBuilder
                                .fromPath("/login")
                                .queryParam("redirect_uri", "{redirect_uri}")
                                .build(request.uri().toString()))
                        .build());
    }

    /**
     * 项目首页（重定向到登录页面）
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    public Mono<ServerResponse> indexPage(ServerRequest serverRequest) {
        log.debug(serverRequest.methodName());
        return getBodyBuilder().syncBody(indexPage);
    }

    /**
     * 登录页面
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    public Mono<ServerResponse> loginPage(ServerRequest serverRequest) {
        log.debug(serverRequest.methodName());
        return getBodyBuilder().render("login");
    }

    /**
     * 登录完成首页
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    public Mono<ServerResponse> homePage(ServerRequest serverRequest) {
        log.debug(serverRequest.methodName());
        return getBodyBuilder().render("home");
    }

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
                .map(token -> (TokenVO) token)
                .filter(TokenVO::isStatus)
                .map(token -> request
                        .queryParam("redirect_uri")
                        .map(redirectUri -> getCookieBodyBuilder(token)
                                .render("redirect:" + UriComponentsBuilder
                                        .fromHttpUrl(URLDecoder.decode(redirectUri))
                                        .build()
                                        .toUri())
                        )
                        .orElseGet(() -> getCookieBodyBuilder(token).render("redirect:home")))
                .orElseGet(() -> status(HttpStatus.UNAUTHORIZED).render("login"));
    }

    /**
     * 返回登录接口的数据
     *
     * @author lujiewen
     * @since 2018/12/28
     */
    public Mono<ServerResponse> token(ServerRequest request) {
        return request
                .attribute(MVW_VALIDATE_TOKEN)
                .map(token -> (TokenVO) token)
                .map(tokenVO -> ok().body(Mono.just(tokenVO), TokenVO.class))
                .orElseGet(() -> ok().body(Mono.just(TokenVO.builder().status(false).build()), TokenVO.class));
    }

    /**
     * 验证接口参数token是否正确
     *
     * @author lujiewen
     * @api {get} /validate 验证令牌
     * @apiName validate
     * @apiVersion 0.0.1
     * @apiGroup validate
     * @apiExample {curl} 接口示例:
     * curl -i https://api.mvwchina.com/basic-auth/validate
     * @apiHeader {String} X-MVW-userID 请求头必须携带字段X-MVW-userID
     * @apiHeader {String} access-key 请求头必须携带字段access-key
     * @apiHeaderExample {json} 头部示例:
     * {
     * "X-MVW-userID": "用户ID",
     * "access-key": "按照约定加密方式产生的token=="
     * }
     * @apiSuccess {Boolean} status 验证是否成功
     * @apiSuccessExample {json} 响应示例:
     * HTTP/1.1 200 OK
     * {
     * "status": true
     * }
     * @since 2018/12/28
     */
    public Mono<ServerResponse> validate(ServerRequest serverRequest) {
        val status = serverRequest.attribute(MVW_VALIDATE_STATUS)
                .map(flag -> (boolean) flag)
                .orElse(false);
        val mono = Mono.just(ValidateVO.builder().status(status).build());
        return ok().body(mono, ValidateVO.class);


    }

    public ServerRequest logout(ServerRequest request) {
        val httpHeaders = request.headers().asHttpHeaders();

        val userID = Optional
                .ofNullable(request.cookies().getFirst(MVW_USER_ID))
                .map(HttpCookie::getValue)
                .orElse(httpHeaders.getFirst(MVW_USER_ID));

        val device = Optional
                .ofNullable(request.cookies().getFirst(DEVICE_TYPE))
                .map(HttpCookie::getValue)
                .orElse(Optional.ofNullable(httpHeaders.getFirst(DEVICE_TYPE)).orElse(DeviceEnum.PC.name()));

        Optional.ofNullable(userID)
                .map(redisTemplate::boundHashOps)
                .ifPresent(hashOperations -> hashOperations.delete(device));

        return request;
    }

    public Mono<ServerResponse> logoutPage(ServerRequest serverRequest) {
        log.debug(serverRequest.methodName());
        return clearCookieBodyBuilder().render("redirect:/login");


    }

    public Mono<ServerResponse> logoutData(ServerRequest serverRequest) {
        log.debug(serverRequest.methodName());
        return ok().body(Mono.just(ValidateVO.builder().status(true).build()), ValidateVO.class);
    }
}

