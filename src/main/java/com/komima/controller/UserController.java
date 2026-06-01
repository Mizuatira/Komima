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

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterDTO dto) {
        return ApiResponse.success("注册成功", userService.register(dto));
    }

    @PostMapping("/login")
    public ApiResponse<User> login(@Valid @RequestBody LoginDTO dto) {
        return ApiResponse.success("登录成功", userService.login(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable Integer id) {
        return ApiResponse.success(userService.getById(id));
    }

    @GetMapping("/list")
    public ApiResponse<List<User>> list() {
        return ApiResponse.success(userService.getAll());
    }

    @PutMapping("/{id}/profile")
    public ApiResponse<Void> updateProfile(@PathVariable Integer id, @Valid @RequestBody ProfileDTO dto) {
        userService.updateProfile(id, dto);
        return ApiResponse.success("保存成功", null);
    }
}
