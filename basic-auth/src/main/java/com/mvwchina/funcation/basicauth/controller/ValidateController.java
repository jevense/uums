package com.mvwchina.funcation.basicauth.controller;

import com.mvwchina.funcation.basicauth.PkgConst;
import com.mvwchina.vo.ValidateVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

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
public class ValidateController {

    @Value("180")
    private int tokenAliveMobile;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
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
     */
    @ResponseBody
    @GetMapping("validate")
    public ValidateVO handle(@RequestAttribute(PkgConst.X_MVW_VALIDATE) boolean status) {
        return ValidateVO.builder().status(status).build();
    }

    /**
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
     */
    @ResponseBody
    @GetMapping("validateToken")
    public ValidateVO handleToken(@RequestParam String token, HttpServletResponse response) {
        if (!stringRedisTemplate.opsForHash().hasKey(PkgConst.CA_HUMAN_TOKEN_CAID, token)) {
            return ValidateVO.builder().status(false).build();
        }

        Cookie userIdCookie = new Cookie(PkgConst.X_MVW_USER_ID, stringRedisTemplate.<String, String>opsForHash().get(PkgConst.CA_HUMAN_TOKEN_CAID, token));
        int duration = (int) Duration.ofDays(tokenAliveMobile * 100).getSeconds();
        userIdCookie.setMaxAge(duration);
        userIdCookie.setPath("/");
        response.addCookie(userIdCookie);

        Cookie tokenCookie = new Cookie(PkgConst.ACCESS_KEY, token);
        tokenCookie.setMaxAge(duration);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        /*兼容老的一体化*/
        Cookie previousTokenCookie = new Cookie(PkgConst.PREVIOUS_TOKEN, token);
        previousTokenCookie.setMaxAge(duration);
        previousTokenCookie.setPath("/");
        response.addCookie(previousTokenCookie);

        Cookie noSecurityCookie = new Cookie(PkgConst.NO_SECURITY_COOKIE, "true");
        noSecurityCookie.setMaxAge(duration);
        noSecurityCookie.setPath("/");
        response.addCookie(noSecurityCookie);

        /*兼容老的一体化*/
        return ValidateVO.builder().status(true).build();
    }

    @GetMapping("logout")
    public String logout(@CookieValue(PkgConst.X_MVW_USER_ID) String userID,
                         @CookieValue(value = PkgConst.DEVICE_TYPE, defaultValue = "PC") String deviceType,
                         HttpServletResponse response) {

        //删除Cookie
        Cookie userIdCookie = new Cookie(PkgConst.X_MVW_USER_ID, null);
        userIdCookie.setMaxAge(0);
        response.addCookie(userIdCookie);
        Cookie tokenCookie = new Cookie(PkgConst.ACCESS_KEY, null);
        tokenCookie.setMaxAge(0);
        response.addCookie(tokenCookie);
        /* set Cookie end */

        /* set redis start */
        redisTemplate.boundHashOps(userID).delete(deviceType);
        /* set redis start */
        return "redirect:/login";

    }
}
