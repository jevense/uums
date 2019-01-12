package com.mvwchina.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Name: SubjectEnum
 * Description:
 * Copyright: Copyright (c)  MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 下午3:01
 */
public enum SubjectEnum {

    General("0700", "全科"), //
    Internal("0100", "内科"), //
    Pediatrics("0200", "儿科"), //
    Emergency("0300", "急诊科"), //
    Dermatology("0400", "皮肤科"), //
    Psychiatry("0500", "精神科"), //
    Neurology("0600", "神经内科"), //
    Rehabilitation("0800", "康复医学科"), //
    Surgery("0900", "外科"), //
    Neurosurgery("1000", "外科-神经外科方向"), //
    Cardiothoracic("1100", "外科-胸心外科方向"), //
    Urinary("1200", "外科-泌尿外科方向"), //
    Plastic("1300", "外科-整形外科方向"), //
    Orthopaedics("1400", "骨科"), //
    Pediatric("1500", "儿外科"), //
    Gynaecology("1600", "妇产科"), //
    Ophthalmology("1700", "眼科"), //
    Otorhinolaryngology("1800", "耳鼻咽喉科"), //
    Anesthesia("1900", "麻醉科"), //
    Nuclear("2000", "临床病理科"), //
    Genetic("2100", "检验医学科"), //
    Imaging("2200", "放射科"), //
    Ultrasound("2300", "超声医学科"), //
    NuclearMedicine("2400", "核医学科"), //
    Radiotherapy("2500", "放射肿瘤科"), //
    Genetics("2600", "医学遗传科"), //
    Prevention("2700", "预防医学科"), //
    Oral("2800", "口腔全科"), //
    OralInternal("2900", "口腔内科"), //
    Maxillofacial("3000", "口腔颌面外科"), //
    Prosthodontics("3100", "口腔修复科"), //
    Orthodontics("3200", "口腔正畸科"), //
    Pathology("3300", "口腔病理科"), //
    MaxillofacialImaging("3400", "口腔颌面影像科");

    final static Map<String, String> valueMap = new HashMap<>();

    static {
        for (SubjectEnum subject : SubjectEnum.values()) {
            valueMap.put(subject.key, subject.name);
        }
    }

    private String key;

    private String name;

    SubjectEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static String parse(String value) {
        return valueMap.get(value);
    }


    public String getKey() {
        return key;
    }


    public String getName() {
        return name;
    }

}
