package com.mvwchina.uums.service.impl;

import com.mvwchina.uums.dao.UserDao;
import com.mvwchina.uums.domain.user.User;
import com.mvwchina.uums.exception.MovedPermanentlyException;
import com.mvwchina.uums.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User getById(Long id) {
        return userDao.getOne(id);
    }

    @Override
    public Page<User> getList(Pageable pageable) {
        return userDao.findAll(pageable);
    }

    @Override
    public User put(Long id, User user) {
        if (getById(id) == null) throw new MovedPermanentlyException();

        user.setId(id);
        return userDao.save(user);
    }

    @Override
    public User post(User user) {
        return userDao.save(user);
    }

    @Override
    public void delete(Long id) {
        if (getById(id) == null) throw new MovedPermanentlyException();
        userDao.deleteById(id);
    }

    @Override
    public User patch(Long id, Map<String, Object> map) {
        User user = getById(id);
        if (user == null) throw new MovedPermanentlyException();

        Class<?> userClass = user.getClass();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                userClass.getField(entry.getKey()).set(user, entry.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("字段{}不满足邀请", entry.getKey());
            }
        }
        return userDao.save(user);
    }
}
