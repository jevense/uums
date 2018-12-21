package com.mvwchina.funcation.basicauth;

import com.mvwchina.enumeration.DeviceEnum;
import com.mvwchina.funcation.basicauth.util.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/21 下午12:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testOpsForHash() {
        //[token,deviceid,appid,expire]
        String userId = UUID.randomUUID().toString();

        Date expireDate = Date.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant());
        String token = MD5.encode(expireDate.toString(), DeviceEnum.PC.name());

        Object[] tokenResult = new Object[]{token, DeviceEnum.PC.name(), "mvwchina", expireDate};
        redisTemplate.opsForHash().put(userId, DeviceEnum.PC.name(), tokenResult);

    }

    @Test
    public void testDel() {
        redisTemplate.delete("*");
    }


}