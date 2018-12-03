package com.olcow.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.olcow.chat.entity.Message;
import com.olcow.chat.until.GetCurrentTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {

    @Resource
    private RedisTemplate redisTemplate;

    @ResponseBody
    @RequestMapping("/getmessage")
    public Map getMessage(@RequestParam(value = "session")
                                      String session){
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(session).toString());
        } catch (Exception e){
            Map map = new HashMap();
            map.put("error","no login");
            return map;
        }
        HashMap<Integer,Object> map;
        map = (HashMap<Integer, Object>) redisTemplate.opsForHash().entries("chat:"+jsonObject.getInteger("uid"));
        return map;
    }

    @ResponseBody
    @RequestMapping("/sendmessage")
    public String sendMessage(@RequestParam(value = "session")
                                          String session,
                              @RequestParam(value = "buid")
                                          Integer bUid,
                              @RequestParam(value = "message")
                                          String message){
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(session).toString());
        } catch (Exception e){
            return "no login";
        }
        List<Message> addFList = (List<Message>) redisTemplate.opsForHash().get("chat:"+jsonObject.getInteger("uid"),bUid);
        if (addFList == null){
            addFList = new ArrayList<>();
        }
        addFList.add(new Message(GetCurrentTime.getCurrentTime(),message));
        HashMap<Integer,List<Message>> map = new HashMap<>();
        map.put(bUid,addFList);
        redisTemplate.opsForHash().putAll("chat:"+jsonObject.getInteger("uid"),map);
        return "successful";
    }
}
