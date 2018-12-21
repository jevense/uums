package com.mvwchina.funcation.basicauth.controller;

import com.google.common.collect.Lists;
import com.mvwchina.enumeration.DeviceEnum;
import com.mvwchina.enumeration.LoginTypeEnum;
import com.mvwchina.funcation.basicauth.domain.Account;
import com.mvwchina.funcation.basicauth.service.AccountService;
import com.mvwchina.funcation.basicauth.util.MD5;
import com.mvwchina.funcation.basicauth.vo.LoginVO;
import com.mvwchina.funcation.basicauth.vo.TokenVO;
import com.mvwchina.util.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;


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
@Slf4j
@Controller
@RequestMapping("login")
public class LoginController {

    @Resource
    private AccountService accountService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("3")
    private int tokenAlive;

    @GetMapping(produces = "text/html;charset=utf-8")
    public String loginPage() {
        return "login";
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
    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenVO loginHandle(@RequestBody LoginVO loginVO) {
        return new TokenVO();
    }

    @PostMapping(produces = "text/html;charset=utf-8")
    public String loginPage(
            @RequestParam String account,
            @RequestParam String password,
            @RequestParam(required = false) String appId,
            @RequestParam(required = false, defaultValue = "PHONE_AND_PASSWORD") LoginTypeEnum loginTypeEnum,
            @RequestParam(required = false, defaultValue = "PC") DeviceEnum deviceEnum,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
            HttpServletResponse response
    ) {

        LoginVO loginVO = new LoginVO();
        loginVO.setAccount(account);
        loginVO.setPassword(password);
        loginVO.setAppId(appId);
        loginVO.setLoginTypeEnum(loginTypeEnum);
        loginVO.setDeviceEnum(deviceEnum);

        Optional<Account> accountOptional = accountService.findByPhone(account);

        if (!accountOptional.isPresent() || !accountService.auth(loginVO, accountOptional.get())) {
            return "login";
        }

        //3天后过期
        Date expireDate = Date.from(LocalDateTime.now().plusDays(tokenAlive).atZone(ZoneId.systemDefault()).toInstant());
        String token = MD5.encode(expireDate.toString(), loginVO.getDeviceEnum().name());

        /* set Cookie start */
        Cookie userIdCookie = new Cookie("X-MVW-userID", accountOptional.get().getUseId());
        userIdCookie.setMaxAge(tokenAlive * 24 * 3600);
        response.addCookie(userIdCookie);

        Cookie tokenCookie = new Cookie("access-key", token);
        tokenCookie.setMaxAge(tokenAlive * 24 * 3600);
        response.addCookie(tokenCookie);
        /* set Cookie end */

        /* set redis start */
        //[token,deviceid,appid,expire]
        List tokenResult = Lists.newArrayList(token, loginVO.getDeviceEnum(), loginVO.getAppId(), expireDate);
        redisTemplate.opsForHash().put(accountOptional.get().getUseId(), loginVO.getDeviceEnum().name(), tokenResult);
        /* set redis start */

        return "redirect:" + URLDecoder.decode(redirectUri, "home");
    }


}
