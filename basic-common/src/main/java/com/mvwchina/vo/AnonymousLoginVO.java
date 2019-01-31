package com.mvwchina.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2019 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2019/1/16 下午3:34
 */
@Data
public class AnonymousLoginVO {

    @NotBlank(message = "用户名不能为空")
    private String account;

    @NotNull(message = "机构不能为空")
    private String instituteNumber;
}
