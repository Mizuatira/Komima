package com.komima.service.impl;

import com.komima.dto.TaskDTO;
import com.komima.entity.Task;
import com.komima.enums.TaskStatus;
import com.komima.exception.BusinessException;
import com.komima.mapper.TaskMapper;
import com.komima.mapper.UserMapper;
import com.komima.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Task publish(TaskDTO taskDTO) {
        if (userMapper.selectById(taskDTO.getUserId()) == null) {
            throw new BusinessException(404, "用户不存在");
        }
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setContent(taskDTO.getContent());
        task.setUserId(taskDTO.getUserId());
        task.setStatus(TaskStatus.PENDING.getCode());
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        taskMapper.insert(task);
        return task;
    }

    @Override
    public List<Task> listAll() {
        return taskMapper.selectAll();
    }

    @Override
    public List<Task> listByUserId(Integer userId) {
        return taskMapper.selectByUserId(userId);
    }

    @Override
    public List<Task> listByReceiverId(Integer receiverId) {
        return taskMapper.selectByReceiverId(receiverId);
    }

    @Override
    public Task getById(Integer id) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new BusinessException(404, "委托不存在");
        }
        return task;
    }

    @Override
    @Transactional
    public void acceptTask(Integer taskId, Integer userId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(404, "委托不存在");
        }
        if (task.getUserId().equals(userId)) {
            throw new BusinessException("不能接自己发布的委托");
        }
        if (task.getStatus() != TaskStatus.PENDING.getCode()) {
            throw new BusinessException("该委托已被接单或已完成");
        }
        taskMapper.updateReceiver(taskId, userId);
        taskMapper.updateStatus(taskId, TaskStatus.ACCEPTED.getCode());
    }

    @Override
    @Transactional
    public void updateStatus(Integer taskId, Integer status, Integer userId) {
        TaskStatus targetStatus = TaskStatus.fromCode(status);
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(404, "委托不存在");
        }
        boolean isPublisher = task.getUserId().equals(userId);
        boolean isReceiver = userId.equals(task.getReceiverId());
        if (!isPublisher && !isReceiver) {
            throw new BusinessException(403, "无权限修改该委托状态");
        }
        if (targetStatus == TaskStatus.COMPLETED) {
            if (!TaskStatus.canComplete(TaskStatus.fromCode(task.getStatus()))) {
                throw new BusinessException("只有已接单状态的委托才能标记为完成");
            }
        }
        taskMapper.updateStatus(taskId, status);
    }
}
