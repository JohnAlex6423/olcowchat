package com.olcow.chat.dao;

import com.olcow.chat.entity.UserFriend;
import com.olcow.chat.entity.UserInfo;

import java.util.List;

public interface UserDao {

    List<UserInfo> selectUserInfoByUids(List<Integer> uids);

    int insertFriend(Integer auid,Integer buid);

    UserFriend selectFriendByABuid(Integer auid, Integer buid);

    List<Integer> selectAuidByBuid(Integer uid);

    List<Integer> selectBuidByAuid(Integer uid);

    List<UserInfo> selectUserInfoLikeNickname(String nickname);

    List<UserInfo> selectUserInfoLikeUid(Integer uid);
}