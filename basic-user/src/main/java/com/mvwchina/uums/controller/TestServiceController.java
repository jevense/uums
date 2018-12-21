package com.mvwchina.uums.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 上午10:20
 */
@Slf4j
@Controller
@RequestMapping("services")
public class TestServiceController {

    @GetMapping
    public String list() {
        return "index";
    }

    @PostMapping
    public String post(@RequestParam(required = false) String account) {
//        System.out.println(redirectUri);
        System.out.println(account);
        return "index";
    }

}
