package com.mvwchina.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/21 上午12:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    @NotBlank(message = "用户名不能为空")
    private String account;

    @NotNull(message = "密码不能为空")
    private String password;

}
