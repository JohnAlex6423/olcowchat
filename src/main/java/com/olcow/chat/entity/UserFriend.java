package com.olcow.chat.entity;

public class UserFriend {

    private Integer auid;

    public Integer getAuid() {
        return auid;
    }

    public void setAuid(Integer auid) {
        this.auid = auid;
    }

    public Integer getBuid() {
        return buid;
    }

    public void setBuid(Integer buid) {
        this.buid = buid;
    }

    private Integer buid;


    public UserFriend(Integer auid, Integer buid) {
        this.auid = auid;
        this.buid = buid;
    }

    public UserFriend() {
    }
}
