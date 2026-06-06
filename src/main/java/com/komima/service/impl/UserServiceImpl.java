package com.komima.service.impl;

import com.komima.dto.*;
import com.komima.entity.Task;
import com.komima.entity.User;
import com.komima.exception.BusinessException;
import com.komima.mapper.ApplicationMapper;
import com.komima.mapper.EvaluateMapper;
import com.komima.mapper.TaskMapper;
import com.komima.mapper.UserMapper;
import com.komima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 * 负责处理用户相关的业务逻辑，包括注册、登录、个人资料管理、用户管理等功能
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private EvaluateMapper evaluateMapper;

    /**
     * 用户注册
     * 新用户注册时调用此方法，会：
     * - 验证用户名是否已存在
     * - 设置用户角色为普通用户（0）
     * - 设置昵称（默认为用户名）
     * - 记录创建时间
     *
     * @param dto 注册数据传输对象，包含用户名、密码、昵称等信息
     * @return 创建的用户记录
     * @throws BusinessException 当用户名已存在时抛出业务异常
     */
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

    /**
     * 用户登录
     * 用户登录时调用此方法，会：
     * - 验证用户是否存在
     * - 验证密码是否正确
     *
     * @param dto 登录数据传输对象，包含用户名和密码
     * @return 用户记录
     * @throws BusinessException 当用户不存在或密码错误时抛出业务异常
     */
    @Override
    public User login(LoginDTO dto) {
        User user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) throw new BusinessException(404, "用户不存在");
        if (!user.getPassword().equals(dto.getPassword())) throw new BusinessException(403, "密码错误");
        return user;
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户记录
     * @throws BusinessException 当用户不存在时抛出业务异常
     */
    @Override
    public User getById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) throw new BusinessException(404, "用户不存在");
        return user;
    }

    /**
     * 获取所有用户列表
     *
     * @return 用户记录列表
     */
    @Override
    public List<User> getAll() {
        return userMapper.selectAll();
    }

    /**
     * 更新用户个人资料
     * 用户可以更新自己的个人信息，包括：
     * - 昵称
     * - 性别
     * - 学院
     * - 专业
     * - 电话
     * - 微信
     * - QQ
     * - 邮箱
     *
     * @param id 用户ID
     * @param dto 个人资料数据传输对象
     * @throws BusinessException 当用户不存在时抛出业务异常
     */
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

    /**
     * 更新用户角色（管理员操作）
     *
     * @param id 用户ID
     * @param role 角色值（0：普通用户，1：管理员）
     * @throws BusinessException 当用户不存在时抛出业务异常
     */
    @Override
    @Transactional
    public void updateRole(Integer id, Integer role) {
        getById(id);
        userMapper.updateRole(id, role);
    }

    /**
     * 删除用户（管理员操作）
     * 删除用户时会级联删除：
     * - 用户发布的所有委托
     * - 与这些委托相关的申请记录
     * - 与这些委托相关的评价记录
     * - 用户本身
     *
     * @param id 用户ID
     * @throws BusinessException 当用户不存在时抛出业务异常
     */
    @Override
    @Transactional
    public void deleteUser(Integer id) {
        getById(id);
        List<Task> tasks = taskMapper.selectByUserId(id);
        for (Task task : tasks) {
            evaluateMapper.deleteByTaskId(task.getId());
            applicationMapper.deleteByTaskId(task.getId());
            taskMapper.deleteById(task.getId());
        }
        userMapper.deleteById(id);
    }
}
