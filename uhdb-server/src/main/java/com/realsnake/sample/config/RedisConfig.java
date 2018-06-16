/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 7.
 */
package com.realsnake.sample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <pre>
 * Class Name : RedisConfig.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 7.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 7.
 * @version 1.0
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String springRedisHost;

    @Value("${spring.redis.port}")
    private Integer springRedisPort;

    @Value("${spring.redis.password}")
    private String springRedisPassword;

    /**
     * http://arcsit.tistory.com/entry/SpringRedis-%EC%8A%A4%ED%94%84%EB%A7%81%EA%B3%BC-%EB%A0%88%EB%94%94%EC%8A%A4-%EC%97%B0%EB%8F%99<br />
     * http://kingbbode.tistory.com/25<br />
     * https://gs.saro.me/#!m=elec&jn=783
     *
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(this.springRedisHost);
        jedisConnectionFactory.setPort(this.springRedisPort);
        jedisConnectionFactory.setPassword(this.springRedisPassword);
        // jedisConnectionFactory.setDatabase(0); // 기본값 0
        jedisConnectionFactory.setTimeout(0);
        jedisConnectionFactory.setUsePool(true);

        // System.out.println("######################################################### JedisConnectionFactory #########################################################");

        return jedisConnectionFactory;
    }

    @Bean
    public StringRedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(this.jedisConnectionFactory());
        return stringRedisTemplate;
    }

}
