package com.mvwchina.uums.domain.user;

import com.mvwchina.domain.BaseEntity;
import com.mvwchina.enumeration.*;
import com.mvwchina.uums.domain.institute.Department;
import com.mvwchina.uums.domain.institute.Subject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Name: User
 * Description: 用户信息主表
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 上午10:17
 */
@Getter
@Setter
@Entity
public class User extends BaseEntity {

    private String accountId;

    /* 用户姓名 */
    private String name;

    /* 性别 */
    @Enumerated(EnumType.STRING)
    private GenderEnum genderEnum;

    /* 系统备注 */
    @Column(length = 200)
    private String comments;

    /* 邮箱 */
    private String email;

    /* 备注 */
    private String remark;

    /* 头像 */
    private String portrait;

    /* 科室 */
    @ManyToOne
    private Department department;

    /* 身份类型 */
    @Enumerated(EnumType.STRING)
    private IdentyTypeEnum identyTypeEnum;

//    21	traineemajorcode	varchar	50	Y	参培专业编码	外键	1	业务信息	修改为当前专业

    /* 参培年份 */
    private String traineeYear;

    /* 是否获得医师资格证书 */
    private Boolean hasLicense;

    /* 医师资格证书编码 */
    private String licenseNum;

    /* 出生日期 */
    private Date birthday;

    /* 民族 */
    @Enumerated(EnumType.STRING)
    private NationalityTypeEnum nationalityTypeEnum;

    /* qq */
    private String qq;

    /* 微信 */
    private String wechat;

    /* 学历 */
    @Enumerated(EnumType.STRING)
    private EducationEnum educationEnum;

    /* 学位 */
    @Enumerated(EnumType.STRING)
    private DegreeEnum degreeEnum;

    /* 学位类型 */
    @Enumerated(EnumType.STRING)
    private DegreeTypeEnum degreeTypeEnum;

    /* 毕业院校 */
    private String graduationCollege;

    /* 毕业专业 */
    private String graduationMajor;

    /* 毕业年份 */
    private String graduationYear;

    /* 专业技术职称 */
    @Enumerated(EnumType.STRING)
    private TitleEnum titleEnum;

    /* 现任职务 */
    private String occupation;

    /* 现职聘用时间 */
    private Date occupationStart;

    /* 是否中级满三年 */
    private Boolean interMediateOver3Years;

    /* 起始带教时间 */
    private Date teachingStart;

    /* 带教年限 */
    private String teachingAge;

    /* 可带教专业 */
    private Subject subject;

    /* 应届生/往届生 */
    private GraduateEnum graduateEnum;

    /* 来参加培训人员的原工作单位 */
    private String everUnit;


}
