package com.mvwchina.funcation.basicauth.service;

import com.mvwchina.funcation.basicauth.domain.previous.Human;
import com.mvwchina.funcation.basicauth.repository.HumanRepository;
import com.mvwchina.util.MD5;
import com.mvwchina.vo.LoginVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/21 上午1:03
 */
@Service
public class HumanService {

    @Resource
    private HumanRepository humanRepository;

    public Optional<Human> findByAccount(String phone) {
        return humanRepository.findOne((root, query, cb) -> cb.and(
                cb.or(cb.equal(root.get("account"), phone),
                        cb.equal(root.get("cellphone"), phone),
                        cb.equal(root.get("identificationNumber"), phone),
                        cb.equal(root.get("email"), phone)),
                cb.equal(root.get("deleted"), false)
        ));

    }

    public boolean auth(LoginVO loginVO, Human human) {
        String password = MD5.encode(loginVO.getPassword(), "");
        return password.equals(human.getPwd());
    }

    public Human save(Human human) {
        return humanRepository.save(human);
    }


}
