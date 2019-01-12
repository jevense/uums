package com.mvwchina.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum DegreeTypeEnum {

    SCIENCE("1", "医学科学学位"),
    MEDICAL("2", "医学专业学位");

    final static Map<String, DegreeTypeEnum> valueMap = new HashMap<>();

    static {
        for (DegreeTypeEnum entity : DegreeTypeEnum.values()) {
            valueMap.put(entity.key, entity);
        }
    }

    private String key;
    private String name;

    DegreeTypeEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static DegreeTypeEnum parseEnum(String key) {
        return valueMap.get(key);
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}
