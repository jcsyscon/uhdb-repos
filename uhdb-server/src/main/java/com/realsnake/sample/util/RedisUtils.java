/**
 *
 */
package com.realsnake.sample.util;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * Class Name : RedisUtils.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 8. 25.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 8. 25.
 * @version 1.0
 */
@Component
public class RedisUtils {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // @Autowired
    // private RedisTemplate<String, String> template;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    /* @formatter:off */
    /**
    public void init() {
        this.valueOperations.set("foo", "bar");
        // this.valueOperations.get("foo");

        // list put
        // String task = listOperations.leftPop("test:task"); while (task != null) { }
        this.listOperations.rightPush("test:task", "자기소개");
        this.listOperations.rightPush("test:task", "취미소개");
        this.listOperations.rightPush("test:task", "소망소개");
        this.listOperations.rightPush("test:task", "선임이직");

        // hash put
        // Map<String, String> intro = hashOperations.entries("test:user:kingbbode");
        this.hashOperations.put("test:user:kingbbode", "name", "권뽀대");
        this.hashOperations.put("test:user:kingbbode", "age", "28");

        // set put
        // Set<String> hobbys = setOperations.members("test:user:kingbbode:hobby");
        this.setOperations.add("test:user:kingbbode:hobby", "개발");
        this.setOperations.add("test:user:kingbbode:hobby", "잠");
        this.setOperations.add("test:user:kingbbode:hobby", "옷 구경");

        // zset
        // zSetOperations.incrementScore("test:user:kingbbode:wish", "경력직 채용", -1);
        this.zSetOperations.add("test:user:kingbbode:wish", "배포한 것에 장애없길", 1);
        this.zSetOperations.add("test:user:kingbbode:wish", "배포한거 아니여도 장애없길", 2);
        this.zSetOperations.add("test:user:kingbbode:wish", "경력직 채용", 3);
        this.zSetOperations.add("test:user:kingbbode:wish", "잘자기", 4);
    }
    */
    /* @formatter:on */

}
