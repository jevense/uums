package com.mvwchina.funcation.basicauth.domain;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvwchina.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Name: User
 * Description: 帐号信息表
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
public class Account extends BaseEntity {

    /* 手机号 */
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '手机号'")
    private String cellphone;

    /* 用户ID */
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT 'useId'")
    private String useId;

    /* 国家编码 */
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '国家编码'")
    private String countryCode;

    /* 密码 */
//    @JsonIgnore
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '密码'")
    private String password;

    /* 帐号锁定截止时间 */
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lockedUntil;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    private AccountAttachment accountAttachment;

}
