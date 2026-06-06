package com.komima.service;

import com.komima.dto.LoginDTO;
import com.komima.dto.ProfileDTO;
import com.komima.dto.RegisterDTO;
import com.komima.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
public interface UserService {

    User register(RegisterDTO dto);

    User login(LoginDTO dto);

    User getById(Integer id);

    List<User> getAll();

    void updateProfile(Integer id, ProfileDTO dto);

    void updateRole(Integer id, Integer role);

    void deleteUser(Integer id);
}
