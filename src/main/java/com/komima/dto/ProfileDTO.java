package com.komima.dto;

import lombok.Data;

/**
 * 个人资料数据传输对象
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Data
public class ProfileDTO {

    private String nickname;
    private Integer gender;
    private String college;
    private String major;
    private String phone;
    private String wechat;
    private String qq;
    private String email;
}
