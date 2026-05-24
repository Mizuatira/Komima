package com.komima.service;

import com.komima.dto.TaskDTO;
import com.komima.entity.Task;

import java.util.List;

public interface TaskService {

    Task publish(TaskDTO taskDTO);

    List<Task> listAll();

    List<Task> listByUserId(Integer userId);

    List<Task> listByReceiverId(Integer receiverId);

    Task getById(Integer id);

    void acceptTask(Integer taskId, Integer userId);

    void updateStatus(Integer taskId, Integer status, Integer userId);
}
