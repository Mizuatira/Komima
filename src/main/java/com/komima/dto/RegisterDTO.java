package com.komima.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 注册数据传输对象
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;
}
