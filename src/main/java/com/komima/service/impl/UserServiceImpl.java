package com.komima.service.impl;

import com.komima.dto.*;
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
        if (userMapper.selectByUsername(dto.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User()
                .setUsername(dto.getUsername())
                .setPassword(dto.getPassword())
                .setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername())
                .setRole(0)
                .setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(LoginDTO dto) {
        User user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) throw new BusinessException(404, "用户不存在");
        if (!user.getPassword().equals(dto.getPassword())) throw new BusinessException(403, "密码错误");
        return user;
    }

    @Override
    public User getById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) throw new BusinessException(404, "用户不存在");
        return user;
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectAll();
    }

    @Override
    @Transactional
    public void updateProfile(Integer id, ProfileDTO dto) {
        getById(id);
        userMapper.updateProfile(new User()
                .setId(id)
                .setNickname(dto.getNickname())
                .setGender(dto.getGender())
                .setCollege(dto.getCollege())
                .setMajor(dto.getMajor())
                .setPhone(dto.getPhone())
                .setWechat(dto.getWechat())
                .setQq(dto.getQq())
                .setEmail(dto.getEmail()));
    }

    @Override
    @Transactional
    public void updateRole(Integer id, Integer role) {
        getById(id);
        userMapper.updateRole(id, role);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        getById(id);
        userMapper.deleteById(id);
    }
}
