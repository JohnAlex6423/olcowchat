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
        List<Integer> addFList = (List<Integer>) redisTemplate.opsForHash().get("chat:"+jsonObject.getInteger("uid"),9);
        if (addFList == null){
            addFList = new ArrayList<>();
        }else {
            for (Integer anAddFList : addFList) {
                if (anAddFList.equals(bUid)) {
                    return "already add";
                }
            }
        }
        addFList.add(bUid);
        HashMap<Integer,List<Integer>> map = new HashMap<>();
        map.put(9,addFList);
        redisTemplate.opsForHash().putAll("chat:"+jsonObject.getInteger("uid"),map);
        return "successful";
    }

    @ResponseBody
    @RequestMapping("/agreeadd")
    public String agreeAdd(@RequestParam(value = "session")
                           String session,
                           @RequestParam(value = "buid")
                           Integer bUid){
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(session).toString());
        } catch (Exception e){
            return "no login";
        }
        List<Integer> fList = (List<Integer>) redisTemplate.opsForHash().get("chat:"+jsonObject.getInteger("uid"),9);
        if (fList==null){
            return "no request";
        }
        for (int i=0;i<fList.size();i++){
            if (fList.get(i).equals(bUid)){
                if (userService.isExsitFriend(jsonObject.getInteger("uid"),bUid)){
                    fList.remove(i);
                    if (fList.isEmpty()){
                        HashMap<Integer,List<Integer>> map = new HashMap<>();
                        map.put(9,fList);
                        redisTemplate.opsForHash().delete("chat:"+jsonObject.getInteger("uid"),9);
                        return "already add";
                    } else {
                        HashMap<Integer,List<Integer>> map = new HashMap<>();
                        map.put(9,fList);
                        redisTemplate.opsForHash().putAll("chat:"+jsonObject.getInteger("uid"),map);
                        return "already add";
                    }
                } else {
                    if (userService.addFriend(jsonObject.getInteger("uid"),bUid) == 1){
                        fList.remove(i);
                        if (fList.isEmpty()){
                            return "successful";
                        } else {
                            HashMap<Integer,List<Integer>> map = new HashMap<>();
                            map.put(9,fList);
                            redisTemplate.opsForHash().putAll("chat:"+jsonObject.getInteger("uid"),map);
                            return "successful";
                        }
                    }else {
                        return "mysql error";
                    }
                }
            }
        }
        return "no request";
    }

    @ResponseBody
    @RequestMapping("/refuseadd")
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
        List<Integer> fList = (List<Integer>) redisTemplate.opsForHash().get("chat:"+jsonObject.getInteger("uid"),9);
        if (fList==null){
            return "no request";
        }
        for (int i=0;i<fList.size();i++){
            if (fList.get(i).equals(bUid)){
                if (userService.isExsitFriend(jsonObject.getInteger("uid"),bUid)){
                    fList.remove(i);
                    if (fList.isEmpty()){
                        return "already add";
                    } else {
                        HashMap<Integer,List<Integer>> map = new HashMap<>();
                        map.put(9,fList);
                        redisTemplate.opsForHash().putAll("chat:"+jsonObject.getInteger("uid"),map);
                        return "already add";
                    }
                } else {
                    fList.remove(i);
                    if (fList.isEmpty()){
                        return "successful";
                    } else {
                        HashMap<Integer,List<Integer>> map = new HashMap<>();
                        map.put(9,fList);
                        redisTemplate.opsForHash().putAll("chat:"+jsonObject.getInteger("uid"),map);
                        return "successful";
                    }
                }
            }
        }
        return "no request";
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
            map.put("error","no login");
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
    @RequestMapping("/getfriendsrequest")
    public Map getFriendsRequest(@RequestParam(value = "session")
                                 String session){
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(session).toString());
        } catch (Exception e){
            Map map = new HashMap();
            map.put("info","no login");
            return map;
        }
        List<Integer> list = (List<Integer>) redisTemplate.opsForHash().get("chat:"+jsonObject.getInteger("uid"),9);
        if (list.isEmpty()){
            Map map = new HashMap();
            map.put("info","no request");
            return map;
        }
        Map map = new HashMap();
        map.put("userinfo",userService.findUserInfoByUids(list));
        return map;
    }

    @ResponseBody
    @RequestMapping("/searchuserinfo/bynicknameoruid")
    public Map searchUserInfoByNicknameOrUid(@RequestParam(value = "content")
                                             String content){
        if (content.length()>6){
            Map map = new HashMap();
            map.put("uid",null);
            map.put("nickname",userService.findUserInfoLikeNickname(content));
            return map;
        }else {
            Integer a;
            try {
                a = Integer.valueOf(content);
            } catch (NumberFormatException e){
                a=null;
            }
            if (a ==null){
                Map map = new HashMap();
                map.put("uid",null);
                map.put("nickname",userService.findUserInfoLikeNickname(content));
                return map;
            }else {
                Map map = new HashMap();
                map.put("uid",userService.findUserInfoLikeUid(a));
                map.put("nickname",userService.findUserInfoLikeNickname(content));
                return map;
            }
        }
    }
}
