package com.mvwchina.funcation.basicauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/2 上午10:49
 */
@Configuration
public class AppConfiguration {

    @Resource
    private Environment environment;

    public int httpPort() {
        return environment.getProperty("http.port", Integer.class, 8081);
    }
}
