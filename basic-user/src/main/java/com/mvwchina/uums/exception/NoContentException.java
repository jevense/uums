package com.mvwchina.uums.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 上午11:55
 */
@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "NO_CONTENT")
public class NoContentException extends RuntimeException {

}

