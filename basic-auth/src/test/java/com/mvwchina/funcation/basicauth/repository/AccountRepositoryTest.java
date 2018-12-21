package com.mvwchina.funcation.basicauth.repository;

import com.mvwchina.funcation.basicauth.domain.Account;
import com.mvwchina.funcation.basicauth.util.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/21 上午11:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTest {

    @Resource
    private AccountRepository accountRepository;

    @Test
    public void testSave() {

        String uuid = UUID.randomUUID().toString();

        Account account = new Account();
        account.setCellphone("15110080976");
        account.setPassword(MD5.encode(MD5.encode("123456", ""), uuid));
        account.setUseId(uuid);

        accountRepository.save(account);

    }

}