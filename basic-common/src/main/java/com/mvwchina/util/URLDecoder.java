package com.mvwchina.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/19 下午1:32
 */
@Slf4j
public class URLDecoder {

    public static String decode(String string) {
        try {
            return java.net.URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String string, String defaultValue) {
        if (Objects.isNull(string)) return defaultValue;
        try {
            return java.net.URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.info("url异常", e);
            return defaultValue;
        }
    }
}
