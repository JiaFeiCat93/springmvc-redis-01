package com.qetch.springmvc.test;

import com.qetch.springmvc.controller.UserController;
import com.qetch.springmvc.dao.UserMapper;
import com.qetch.springmvc.entity.User;
import com.qetch.springmvc.entity.UserExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-redis.xml", "classpath:appcalitionContext.xml"})
public class RedisTest {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserController userController;

    @Test
    public void testRedis() {
        Object mykey = redisTemplate.opsForValue().get("mykey");
        System.out.println("--->" + mykey);
        if (null == mykey) {
            redisTemplate.opsForValue().set("mykey", "1232");
        } else {
            redisTemplate.delete("mykey");
        }
        System.out.println("--->" + redisTemplate.opsForValue().get("mykey"));
    }

    @Test
    public void testDB() {
        UserExample example = new UserExample();
        List<User> users = userMapper.selectByExample(example);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testController() {
        System.out.println(userController.hello());
    }

    @Test
    public void testRedisPool() {
        Jedis jedis = new Jedis("192.168.1.12", 6379);
        System.out.println(jedis.keys("*"));
    }
}
