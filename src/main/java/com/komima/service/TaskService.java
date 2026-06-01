package com.komima.service;

import com.komima.dto.TaskDTO;
import com.komima.entity.Task;

import java.util.List;

public interface TaskService {

    Task publish(TaskDTO dto);

    List<Task> listAll();

    List<Task> listFiltered(Integer status, Integer category, Integer hasReward, Integer excludeUserId);

    List<Task> listByUserId(Integer userId);

    List<Task> listByApplicantId(Integer applicantId);

    Task getById(Integer id);

    void deleteTask(Integer taskId);

    void deleteMyPendingTask(Integer taskId, Integer userId);
}
