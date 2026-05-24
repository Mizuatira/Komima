package com.komima.service.impl;

import com.komima.dto.LoginDTO;
import com.komima.dto.ProfileDTO;
import com.komima.dto.RegisterDTO;
import com.komima.entity.User;
import com.komima.exception.BusinessException;
import com.komima.mapper.UserMapper;
import com.komima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public User register(RegisterDTO dto) {
        User existing = userMapper.selectByUsername(dto.getUsername());
        if (existing != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(0);
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : "momo");
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(LoginDTO dto) {
        User user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new BusinessException(403, "密码错误");
        }
        return user;
    }

    @Override
    public User getById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectAll();
    }

    @Override
    @Transactional
    public void updateProfile(Integer id, ProfileDTO dto) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setNickname(dto.getNickname());
        user.setGender(dto.getGender());
        user.setCollege(dto.getCollege());
        user.setMajor(dto.getMajor());
        user.setPhone(dto.getPhone());
        user.setWechat(dto.getWechat());
        user.setQq(dto.getQq());
        user.setEmail(dto.getEmail());
        userMapper.updateProfile(user);
    }

    @Override
    @Transactional
    public void updateRole(Integer id, Integer role) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        userMapper.updateRole(id, role);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
    }
}
