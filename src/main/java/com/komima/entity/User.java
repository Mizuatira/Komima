package com.komima.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Data
@Accessors(chain = true)
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

    /** 子查询注入 */
    private transient Double avgRating;

    private LocalDateTime createTime;

    public boolean isAdmin() {
        return role != null && role == 1;
    }
}
