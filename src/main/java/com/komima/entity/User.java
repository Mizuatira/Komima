package com.komima.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {

    private Integer id;
    private String username;
    private String password;
    private Integer role;
    private String nickname;
    private Integer gender;
    private String college;
    private String major;
    private String phone;
    private String wechat;
    private String qq;
    private String email;
    private LocalDateTime createTime;

    public boolean isAdmin() {
        return role != null && role == 1;
    }
}
