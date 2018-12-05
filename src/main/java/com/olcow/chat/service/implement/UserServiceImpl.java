package com.olcow.chat.service.implement;

import com.olcow.chat.dao.UserDao;
import com.olcow.chat.entity.UserInfo;
import com.olcow.chat.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public List<UserInfo> findUserInfoByUids(List<Integer> uids) {
        return userDao.selectUserInfoByUids(uids);
    }

    @Override
    public int addFriend(Integer auid, Integer buid) {
        return userDao.insertFriend(auid,buid);
    }

    @Override
    public boolean isExsitFriend(Integer auid, Integer buid) {
        return userDao.selectFriendByABuid(auid,buid)!=null;
    }

    @Override
    public List<UserInfo> findFriends(Integer uid) {
        List<Integer> uidA = userDao.selectAuidByBuid(uid);
        List<Integer> uidB = userDao.selectBuidByAuid(uid);
        uidA.addAll(uidB);
        if (uidA.isEmpty()){
            return null;
        }
        return userDao.selectUserInfoByUids(uidA);
    }

    @Override
    public List<UserInfo> findUserInfoLikeNickname(String nickname) {
        return userDao.selectUserInfoLikeNickname(nickname);
    }

    @Override
    public List<UserInfo> findUserInfoLikeUid(Integer uid) {
        return userDao.selectUserInfoLikeUid(uid);
    }
}
