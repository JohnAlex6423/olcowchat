package com.olcow.chat;

import com.olcow.chat.dao.UserDao;
import com.olcow.chat.entity.UserFriend;
import com.olcow.chat.entity.UserInfo;
import com.olcow.chat.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatApplicationTests {

    @Resource
    UserService userService;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    private UserDao userDao;

	@Test
	public void contextLoads() {
        for (UserInfo userInfo:userDao.selectUserInfoLikeNickname("傻")){
            System.err.println(userInfo.getName());
        }
	}

}
