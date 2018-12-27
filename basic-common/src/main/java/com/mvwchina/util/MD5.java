package com.mvwchina.util;

import org.springframework.util.DigestUtils;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/21 上午2:21
 */
public class MD5 {

    public static String encode(String source, String salt) {
        return DigestUtils.md5DigestAsHex((source + salt).getBytes());
    }
}
