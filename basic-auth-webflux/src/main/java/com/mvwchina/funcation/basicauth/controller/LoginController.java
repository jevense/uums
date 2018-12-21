package com.mvwchina.funcation.basicauth.controller;

import com.mvwchina.util.URLDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/2 下午3:02
 */
@Controller
@RequestMapping(value = "login", produces = "text/html;charset=utf-8")
public class LoginController {

    @GetMapping
    public Mono<ServerResponse> loginPage() {
        return ok().render("login");
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
    @PostMapping
    public Mono<ServerResponse> handle(String account,
                                       String password,
                                       Optional<String> redirectUri) {

        if (redirectUri.isPresent()) {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URLDecoder.decode(redirectUri.get())).build();
            return ServerResponse.temporaryRedirect(uriComponents.toUri()).build();
        }
        return ok().render("home");
    }
}
