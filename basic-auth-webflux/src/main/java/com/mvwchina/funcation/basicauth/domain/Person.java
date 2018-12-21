package com.mvwchina.funcation.basicauth.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/2 下午2:44
 */
@Data
@Entity
public class Person {

    @Id
    private int id;

    public interface WithoutPasswordView {};

    public interface WithPasswordView extends WithoutPasswordView {};
}
