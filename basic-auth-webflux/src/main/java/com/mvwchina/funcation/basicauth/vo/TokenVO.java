package com.mvwchina.funcation.basicauth.vo;

import lombok.Data;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/19 下午2:32
 */
@Data
public class TokenVO {

    private boolean status;

    private String userID;

    private String token;

    private String expiresIn;
}
