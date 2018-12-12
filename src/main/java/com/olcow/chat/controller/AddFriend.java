package com.olcow.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.olcow.chat.entity.UserInfo;
import com.olcow.chat.service.UserService;
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
public class AddFriend {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping("/addfriend")
    public String  addFriend(@RequestParam(value = "session")
                                     String session,
                             @RequestParam(value = "buid")
                                     Integer bUid){
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(session).toString());
        } catch (Exception e){
            return "no login";
        }
        if (jsonObject.getByte("permission")==0){
            return "no activity";
        }
        if (userService.isExsitFriend(jsonObject.getInteger("uid"),bUid)){
            return "already add";
        } else {
            if (userService.addFriend(jsonObject.getInteger("uid"),bUid) == 1){
                return "successful";
            }else {
                return "fail";
            }
        }
    }

    @ResponseBody
    @RequestMapping("/delfriend")
    public String refuseAdd(@RequestParam(value = "session")
                                        String session,
                            @RequestParam(value = "buid")
                                        Integer bUid){
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(session).toString());
        } catch (Exception e){
            return "no login";
        }
        if (userService.delFriendByAuidAndBuid(jsonObject.getInteger("uid"),bUid)==1){
            return "successful";
        } else {
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping("/getfriends")
    public Map getFriends(@RequestParam(value = "session")
                                                 String session){
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(session).toString());
        } catch (Exception e){
            Map map = new HashMap();
            map.put("info","no login");
            return map;
        }
        if (jsonObject.getByte("permission")==0){
            Map map = new HashMap();
            map.put("info","no activity");
            return map;
        }
        List<UserInfo> userInfos = userService.findFriends(jsonObject.getInteger("uid"));
        if (userInfos == null){
            Map map = new HashMap();
            map.put("info","no friend");
            return map;
        }
        Map map = new HashMap();
        map.put("friend",userInfos);
        return map;
    }


    @ResponseBody
    @RequestMapping("/searchuserinfo/bynicknameoruid")
    public Map searchUserInfoByNicknameOrUid(@RequestParam(value = "content")
                                             String content){
        if (content.equals("")){
            Map map = new HashMap();
            map.put("uid",new ArrayList<>());
            map.put("nickname",new ArrayList<>());
            return map;
        }
        if (content.length()>6){
            Map map = new HashMap();
            map.put("uid",new ArrayList<>());
            map.put("nickname",userService.findUserInfoLikeNickname(content));
            return map;
        }else if (content.length()<6&&content.length()>3){
            Integer a;
            try {
                a = Integer.valueOf(content);
            } catch (NumberFormatException e){
                a=null;
            }
            if (a ==null){
                Map map = new HashMap();
                map.put("uid",new ArrayList<>());
                map.put("nickname",userService.findUserInfoLikeNickname(content));
                return map;
            }else {
                Map map = new HashMap();
                map.put("uid",userService.findUserInfoLikeUid(a));
                map.put("nickname",userService.findUserInfoLikeNickname(content));
                return map;
            }
        } else {
            Map map = new HashMap();
            map.put("uid",new ArrayList<>());
            map.put("nickname",userService.findUserInfoLikeNickname(content));
            return map;
        }
    }

    @ResponseBody
    @RequestMapping("/searchpreinfo/bynicknameoruid")
    public Map searchPreUserInfoByNicknameOrUid(@RequestParam(value = "content")
                                                String content){
        if (content.equals("")){
            Map map = new HashMap();
            map.put("uid",null);
            map.put("nickname",null);
            return map;
        }
        if (content.length()>6){
            Map map = new HashMap();
            map.put("uid",null);
            map.put("nickname",userService.findUserInfoByNickname(content));
            return map;
        }else if (content.length()<6&&content.length()>3){
            Integer a;
            try {
                a = Integer.valueOf(content);
            } catch (NumberFormatException e){
                a=null;
            }
            if (a ==null){
                Map map = new HashMap();
                map.put("uid",null);
                map.put("nickname",userService.findUserInfoByNickname(content));
                return map;
            }else {
                Map map = new HashMap();
                map.put("uid",userService.findUserInfoByUid(a));
                map.put("nickname",userService.findUserInfoByNickname(content));
                return map;
            }
        } else {
            Map map = new HashMap();
            map.put("uid",null);
            map.put("nickname",userService.findUserInfoByNickname(content));
            return map;
        }
    }
}
