package com.mvwchina.funcation.basicauth;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/26 下午8:51
 */
@Data
@Configuration
@PropertySource("classpath:define.properties")
public class Define {

    @Value("${mvwchina.token-alive}")
    private int tokenAlive;
}
