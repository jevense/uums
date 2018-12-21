package com.mvwchina.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum EducationEnum {

    UNKNOWN("0", "未定义"),
    ACADEMY("1", "专科及以下"),
    UNDERGRADUATE("2", "本科"),
    MASTER("3", "硕士研究生"),
    DOCTOR("4", "博士研究生"),
    OTHER("5", "其他");

    public static Map<String, EducationEnum> valueMap = new HashMap<>();

    static {
        for (EducationEnum entity : EducationEnum.values()) {
            valueMap.put(entity.key, entity);
        }
    }

    private String key;
    private String name;

    EducationEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static EducationEnum parseEnum(String key) {
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
