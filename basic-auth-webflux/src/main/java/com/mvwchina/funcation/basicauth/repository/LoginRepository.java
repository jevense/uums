package com.mvwchina.funcation.basicauth.repository;

import com.mvwchina.funcation.basicauth.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/2 下午2:45
 */
public interface LoginRepository extends JpaRepository<Account, String> {

}
