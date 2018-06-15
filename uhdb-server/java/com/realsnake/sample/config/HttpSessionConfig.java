package com.realsnake.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.accept.ContentNegotiationStrategy;

import com.realsnake.sample.config.sec.CustomHttpSessionStrategy;

@EnableRedisHttpSession
public class HttpSessionConfig {

    @Value("${server.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    /**
     * 세션서버를 레디스로 사용하는 경우, 세션타임아웃을 지정한다.<br />
     * 주의) HttpSessionListener를 사용할 수 없다.<br />
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisOperationsSessionRepository sessionRepository(RedisConnectionFactory factory) {
        RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(factory);
        sessionRepository.setDefaultMaxInactiveInterval(this.maxInactiveIntervalInSeconds);
        // System.out.println("######################################################### 레디스 세션타임아웃: " + this.maxInactiveIntervalInSeconds);
        return sessionRepository;
    }

    @Autowired
    private ContentNegotiationStrategy contentNegotiationStrategy;

    @Bean
    @Primary
    HttpSessionStrategy httpSessionStrategy() {
        return new CustomHttpSessionStrategy(this.contentNegotiationStrategy); // HeaderHttpSessionStrategy();
    }

}
