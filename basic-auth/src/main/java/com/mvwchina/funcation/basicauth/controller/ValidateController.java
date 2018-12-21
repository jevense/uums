package com.mvwchina.funcation.basicauth.controller;

import com.mvwchina.funcation.basicauth.vo.ValidateVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
@RestController
public class ValidateController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
    @GetMapping("validate")
    public ValidateVO handle(@RequestAttribute("x-mvw-validate") boolean status) {
        return new ValidateVO(status);
    }
}
