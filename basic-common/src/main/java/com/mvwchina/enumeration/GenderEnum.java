package com.mvwchina.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Title. GenderEnum<br>
 * Description.<类的描述>
 * <p>
 * Copyright(c)2015 MVW Co.,Ltd.
 * <p>
 * Company: 北京医视时代科技发展有限公司
 * <p>
 * Author: Jeven<br>
 * Email: lujiewen@cyberzone.cn
 * <p>
 * Date: 2015-2-12 下午1:33:42
 * <p>
 * Version: 1.0
 * <p>
 * 修改人：<修改人><br>
 * 修改时间：<修改时间><br>
 * 修改描述：<修改描述>
 * <p>
 *
 * @version 2015-2-12
 */
public enum GenderEnum {

    UNKNOWN("0", "未知的性别"),
    MALE("1", "男"),
    FEMALE("2", "女"),
    MTF("3", "女性改（变）为男性"),
    FTM("3", "女性改（变）为男性"),
    OTHER("3", "未说明的性别");

    final static Map<String, GenderEnum> valueMap = new HashMap<>();

    static {
        for (GenderEnum gender : GenderEnum.values()) {
            valueMap.put(gender.key, gender);
        }
    }

    private String key;
    private String name;

    GenderEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static GenderEnum parseEnum(String key) {
        if (valueMap.get(key) == null || valueMap.get(key).equals(UNKNOWN)) {
            return null;
        }
        return valueMap.get(key);
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}
