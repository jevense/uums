package com.mvwchina.funcation.basicauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/21 上午3:18
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        return new JsonRedisTemplate(factory);
    }

    class JsonRedisTemplate extends RedisTemplate<String, Object> {

        JsonRedisTemplate() {
            setKeySerializer(RedisSerializer.string());
            setValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
            setHashKeySerializer(RedisSerializer.string());
            setHashValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        }

        JsonRedisTemplate(RedisConnectionFactory connectionFactory) {
            this();
            setConnectionFactory(connectionFactory);
            afterPropertiesSet();
        }
    }
}
