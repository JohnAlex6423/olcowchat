package com.olcow.chat;

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

	@Test
	public void contextLoads() {
//        List<Integer> list = new ArrayList<>();
//        list.add(12345);
//        list.add(123456);
//        list.add(123457);
//        List<UserInfo> list1 = userService.findUserInfoByUids(list);
//        for (UserInfo userInfo:list1){
//            System.err.println(userInfo.getName());
//        }
//        userService.addFriend(10000,10001);
//        System.err.println(userService.isExsitFriend(1001,1000));
//        HashMap<Integer,Object> map = new HashMap();
//        map = (HashMap<Integer, Object>) redisTemplate.opsForHash().entries("chat:43");
//        System.err.println(userService.findFriends(43));
        List<Integer> list = (List<Integer>) redisTemplate.opsForHash().get("chat:43",9);
        for (Integer integer : list){
            System.err.println(integer);
        }
	}

}
