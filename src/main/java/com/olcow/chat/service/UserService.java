package com.olcow.chat.service;

import com.olcow.chat.entity.UserInfo;

import java.util.List;

public interface UserService {

    List<UserInfo> findUserInfoByUids(List<Integer> uids);

    int addFriend(Integer auid, Integer buid);

    boolean isExsitFriend(Integer auid,Integer buid);

    List<UserInfo> findFriends(Integer uid);

    List<UserInfo> findUserInfoLikeNickname(String nickname);

    List<UserInfo> findUserInfoLikeUid(Integer uid);

    UserInfo findUserInfoByUid(Integer uid);

    List<UserInfo> findUserInfoByNickname(String nickname);

    int delFriendByAuidAndBuid(Integer aUid, Integer bUid);
}
