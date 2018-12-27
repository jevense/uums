package com.mvwchina.util;

import lombok.SneakyThrows;
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

    @SneakyThrows(UnsupportedEncodingException.class)
    public static String decode(String string) {
        return java.net.URLDecoder.decode(string, "UTF-8");
    }

    public static String decode(String string, String defaultValue) {
        if (Objects.isNull(string)) return defaultValue;
        return decode(string);
    }
}
