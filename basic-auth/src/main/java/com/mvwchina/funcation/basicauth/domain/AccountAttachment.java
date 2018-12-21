package com.mvwchina.funcation.basicauth.domain;

import com.mvwchina.domain.BaseEntity;
import com.mvwchina.enumeration.IdentificationEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/6 上午11:44
 */
@Getter
@Setter
@Entity
public class AccountAttachment extends BaseEntity {

    @OneToOne
    @PrimaryKeyJoinColumn
    private Account account;

    /* 帐号(系统生成) */
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '帐号(系统生成)'")
    private String accountName;

    /* 证件类型 */
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '证件类型'")
    @Enumerated(EnumType.STRING)
    private IdentificationEnum identificationType;

    /* 证件号码 */
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '证件号码'")
    private String identificationNumber;
}
