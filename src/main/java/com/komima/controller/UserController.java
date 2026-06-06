package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.dto.ProfileDTO;
import com.komima.dto.LoginDTO;
import com.komima.dto.RegisterDTO;
import com.komima.entity.User;
import com.komima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param dto 注册信息
     * @return 用户信息
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterDTO dto) {
        return ApiResponse.success("注册成功", userService.register(dto));
    }

    /**
     * 用户登录
     * @param dto 登录信息
     * @return 用户信息
     */
    @PostMapping("/login")
    public ApiResponse<User> login(@Valid @RequestBody LoginDTO dto) {
        return ApiResponse.success("登录成功", userService.login(dto));
    }

    /**
     * 获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable Integer id) {
        return ApiResponse.success(userService.getById(id));
    }

    /**
     * 获取用户列表
     * @return 用户列表
     */
    @GetMapping("/list")
    public ApiResponse<List<User>> list() {
        return ApiResponse.success(userService.getAll());
    }

    /**
     * 更新用户资料
     * @param id 用户ID
     * @param dto 资料信息
     * @return 响应结果
     */
    @PutMapping("/{id}/profile")
    public ApiResponse<Void> updateProfile(@PathVariable Integer id, @Valid @RequestBody ProfileDTO dto) {
        userService.updateProfile(id, dto);
        return ApiResponse.success("保存成功", null);
    }
}
