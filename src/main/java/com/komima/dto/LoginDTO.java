package com.komima.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录数据传输对象
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
