package com.mvwchina.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum DegreeEnum {

    NONE("0", "无学位"),
    BACHELOR("1", "学士"),
    MASTER("2", "硕士"),
    DOCTOR("3", "博士");

    final static Map<String, DegreeEnum> valueMap = new HashMap<>();

    static {
        for (DegreeEnum entity : DegreeEnum.values()) {
            valueMap.put(entity.key, entity);
        }
    }

    private String key;
    private String name;

    DegreeEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static DegreeEnum parseEnum(String key) {
        if (valueMap.get(key) == null || valueMap.get(key).equals(NONE)) {
            return NONE;
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
