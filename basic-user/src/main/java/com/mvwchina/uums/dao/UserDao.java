package com.mvwchina.uums.dao;

import com.mvwchina.uums.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 上午10:28
 */
public interface UserDao extends JpaRepository<User, Long> {
}
