package com.sharding.jdbc.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @description:
 **/
@TableName("user")
public class User {

    private Long userId;
    private String username;
    private String ustatus;
    private int uage;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUstatus() {
        return ustatus;
    }

    public void setUstatus(String ustatus) {
        this.ustatus = ustatus;
    }

    public int getUage() {
        return uage;
    }

    public void setUage(int uage) {
        this.uage = uage;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", ustatus='" + ustatus + '\'' +
                ", uage=" + uage +
                '}';
    }
}
