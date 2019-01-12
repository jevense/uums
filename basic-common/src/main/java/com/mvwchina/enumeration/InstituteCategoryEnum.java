package com.mvwchina.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum InstituteCategoryEnum {

    HOSPITAL_GENERAL("01", "综合医院"),
    HOSPITAL_CHINESE("02", "中医医院"),
    HOSPITAL_INTEGRATED("03", "中西医结合医院"),
    HOSPITAL_NATIONAL("04", "民族医医院"),
    HOSPITAL_SPECIALIST("05", "专科医院"),
    HEALTH_CENTER_COMMUNITY("06", "社区卫生服务中心"),
    HEALTH_STATION_COMMUNITY("07", "社区卫生服务站"),
    HEALTH_CLINICS_STREET("08", "街道卫生院"),
    HEALTH_CLINICS_TOWNS("09", "乡镇卫生院"),
    CENTRE_MATERNAL_CHILD("10", "妇幼保健院"),
    INSTITUTION_MATERNAL_CHILD("11", "妇幼保健所"),
    STATION_MATERNAL_CHILD("12", "妇幼保健站"),
    SENIOR_SCHOOL("13", "医学普通高中等学校"),
    MEDICAL_ADULT_SCHOOL("14", "医学成人学校"),
    MEDICAL_TRAINING_INSTITUTIONS("15", "医学在职培训机构"),
    OTHER("16", "其他");

    final static Map<String, InstituteCategoryEnum> valueMap = new HashMap<>();

    static {
        for (InstituteCategoryEnum entity : InstituteCategoryEnum.values()) {
            valueMap.put(entity.key, entity);
        }
    }

    private String key;
    private String name;

    InstituteCategoryEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static InstituteCategoryEnum parseEnum(String key) {
        return valueMap.get(key);
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}
