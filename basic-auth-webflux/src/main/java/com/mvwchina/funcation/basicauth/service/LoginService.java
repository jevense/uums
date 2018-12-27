package com.mvwchina.funcation.basicauth.service;

import com.mvwchina.funcation.basicauth.domain.Account;
import com.mvwchina.funcation.basicauth.repository.LoginRepository;
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
public class LoginService {

    @Resource
    private LoginRepository accountRepository;

    public Optional<Account> findByPhone(String phone) {
        return accountRepository.findOne((root, query, cb) -> cb.and(
                cb.equal(root.get("cellphone"), phone),
                cb.equal(root.get("deleted"), false)
        ));

    }

    public boolean auth(LoginVO loginVO, Account account) {
        return MD5.encode(MD5.encode(loginVO.getPassword(), ""), account.getUseId())
                .equals(account.getPassword());
    }


}
