package com.komima.service;

import com.komima.dto.TaskDTO;
import com.komima.entity.Task;

import java.util.List;

/**
 * 任务服务接口
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
public interface TaskService {

    Task publish(TaskDTO dto);

    List<Task> listAll();

    List<Task> listFiltered(Integer status, Integer category, Integer hasReward, Integer excludeUserId);

    List<Task> listByUserId(Integer userId);

    List<Task> listByApplicantId(Integer applicantId);

    Task getById(Integer id);

    void deleteTask(Integer taskId);

    void deleteMyPendingTask(Integer taskId, Integer userId);

    void expireTasks(int days);
}
