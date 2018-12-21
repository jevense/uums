package com.mvwchina.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum TitleEnum {

    PHYSICIAN_DIRECTOR("01", "主任医师"),
    PHYSICIAN_DIRECTOR_DEPUTY("02", "副主任医师"),
    PHYSICIAN_DIRECTOR_EXECUTIVE("03", "主治医师"),
    PHYSICIAN("04", "医师、医士"),
    PHARMACIST_DIRECTOR("05", "主任药师"),
    PHARMACIST_DIRECTOR_DEPUTY("06", "副主任药师"),
    PHARMACIST_DIRECTOR_EXECUTIVE("07", "主管药师"),
    PHARMACIST("08", "药师、药士"),
    NURSE_DIRECTOR("09", "主任护师"),
    NURSE_DIRECTOR_DEPUTY("10", "副主任护师"),
    NURSE_DIRECTOR_EXECUTIVE("11", "主管护师"),
    NURSE("12", "护师、护士"),
    TECHNICIAN_DIRECTOR("13", "主任技师"),
    TECHNICIAN_DIRECTOR_DEPUTY("14", "副主任技师"),
    TECHNICIAN_DIRECTOR_EXECUTIVE("15", "主管技师"),
    TECHNICIAN("16", "技师、技士"),
    NONE("17", "无");

    public static Map<String, TitleEnum> valueMap = new HashMap<>();

    static {
        for (TitleEnum entity : TitleEnum.values()) {
            valueMap.put(entity.key, entity);
        }
    }

    private String key;
    private String name;

    TitleEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static TitleEnum parseEnum(String key) {
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
