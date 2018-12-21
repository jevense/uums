package com.mvwchina.uums.service;

import com.mvwchina.uums.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 上午10:24
 */
public interface UserService {

    User getById(Long id);

    Page<User> getList(Pageable pageable);

    User put(Long id, User user);

    User post(User user);

    void delete(Long id);

    User patch(Long id, Map<String, Object> map);
}
