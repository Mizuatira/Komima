package com.komima.service.impl;

import com.komima.dto.TaskDTO;
import com.komima.entity.Task;
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
    public Task publish(TaskDTO dto) {
        if (userMapper.selectById(dto.getUserId()) == null) {
            throw new BusinessException(404, "用户不存在");
        }
        int rc = dto.getRecruitCount() != null && dto.getRecruitCount() > 0 ? dto.getRecruitCount() : 1;
        Task task = new Task()
                .setTitle(dto.getTitle()).setContent(dto.getContent())
                .setUserId(dto.getUserId()).setCategory(dto.getCategory())
                .setHasReward(dto.getHasReward() != null ? dto.getHasReward() : 0)
                .setRecruitCount(rc)
                .setStatus(0)
                .setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now());
        taskMapper.insert(task);
        return task;
    }

    @Override
    public List<Task> listAll() { return taskMapper.selectAll(); }

    @Override
    public List<Task> listFiltered(Integer status, Integer category, Integer hasReward, Integer excludeUserId) {
        return taskMapper.selectFiltered(status, category, hasReward, excludeUserId);
    }

    @Override
    public List<Task> listByUserId(Integer userId) { return taskMapper.selectByUserId(userId); }

    @Override
    public List<Task> listByApplicantId(Integer applicantId) { return taskMapper.selectByApplicantId(applicantId); }

    @Override
    public Task getById(Integer id) {
        Task task = taskMapper.selectById(id);
        if (task == null) throw new BusinessException(404, "委托不存在");
        return task;
    }

    @Override
    @Transactional
    public void deleteTask(Integer taskId) {
        getById(taskId);
        taskMapper.deleteById(taskId);
    }

    @Override
    @Transactional
    public void deleteMyPendingTask(Integer taskId, Integer userId) {
        Task task = getById(taskId);
        if (!task.getUserId().equals(userId)) throw new BusinessException(403, "只能删除自己的委托");
        if (task.getStatus() != 0) throw new BusinessException("只能删除待接单状态的委托");
        taskMapper.deleteById(taskId);
    }
}
