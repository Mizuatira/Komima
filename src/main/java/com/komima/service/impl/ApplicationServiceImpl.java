package com.komima.service.impl;

import com.komima.entity.Application;
import com.komima.entity.Task;
import com.komima.enums.TaskStatus;
import com.komima.exception.BusinessException;
import com.komima.mapper.ApplicationMapper;
import com.komima.mapper.TaskMapper;
import com.komima.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    @Transactional
    public Application apply(Integer taskId, Integer applicantId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) throw new BusinessException(404, "委托不存在");
        if (task.getUserId().equals(applicantId)) throw new BusinessException("不能申请自己的委托");
        if (task.getStatus() != TaskStatus.PENDING.getCode()) throw new BusinessException("该委托已在进行中或已完成");

        int approved = applicationMapper.countApprovedByTaskId(taskId);
        if (approved >= task.getRecruitCount()) throw new BusinessException("招募人数已满");

        if (applicationMapper.selectByTaskAndApplicant(taskId, applicantId) != null) {
            throw new BusinessException("你已申请过");
        }
        Application app = new Application()
                .setTaskId(taskId).setApplicantId(applicantId)
                .setStatus(0).setConfirmPublisher(0).setConfirmApplicant(0)
                .setCreateTime(LocalDateTime.now());
        applicationMapper.insert(app);
        return app;
    }

    @Override
    public List<Application> listByTask(Integer taskId) {
        return applicationMapper.selectByTaskId(taskId);
    }

    @Override
    @Transactional
    public void approve(Integer taskId, Integer applicantId, Integer publisherId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) throw new BusinessException(404, "委托不存在");
        if (!task.getUserId().equals(publisherId)) throw new BusinessException(403, "仅发布者可操作");

        int approved = applicationMapper.countApprovedByTaskId(taskId);
        if (approved >= task.getRecruitCount()) throw new BusinessException("招募人数已满");

        Application app = applicationMapper.selectByTaskAndApplicant(taskId, applicantId);
        if (app == null) throw new BusinessException(404, "不存在此申请");
        applicationMapper.updateStatus(app.getId(), 1);
        if (task.getStatus() == TaskStatus.PENDING.getCode()) {
            taskMapper.updateStatus(taskId, TaskStatus.IN_PROGRESS.getCode());
        }
    }

    @Override
    @Transactional
    public void reject(Integer taskId, Integer applicantId, Integer publisherId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) throw new BusinessException(404, "委托不存在");
        if (!task.getUserId().equals(publisherId)) throw new BusinessException(403, "仅发布者可操作");
        Application app = applicationMapper.selectByTaskAndApplicant(taskId, applicantId);
        if (app == null) throw new BusinessException(404, "不存在此申请");
        applicationMapper.updateStatus(app.getId(), 2);
    }

    @Override
    @Transactional
    public void confirmComplete(Integer applicationId, Integer userId) {
        Application app = applicationMapper.selectById(applicationId);
        if (app == null) throw new BusinessException(404, "申请不存在");
        if (app.getStatus() != 1) throw new BusinessException("该申请尚未通过");

        Task task = taskMapper.selectById(app.getTaskId());
        boolean isPub = task.getUserId().equals(userId);
        boolean isSelf = app.getApplicantId().equals(userId);
        if (!isPub && !isSelf) throw new BusinessException(403, "无权限");

        if (isPub) {
            if (isTrue(app.getConfirmPublisher())) throw new BusinessException("你已确认");
            applicationMapper.confirmPublisher(applicationId);
        } else {
            if (isTrue(app.getConfirmApplicant())) throw new BusinessException("你已确认");
            applicationMapper.confirmApplicant(applicationId);
        }

        int totalApproved = applicationMapper.countApprovedByTaskId(app.getTaskId());
        int totalConfirmed = applicationMapper.countConfirmedByTaskId(app.getTaskId());
        if (totalConfirmed >= totalApproved) {
            taskMapper.updateStatus(app.getTaskId(), TaskStatus.COMPLETED.getCode());
        }
    }

    private boolean isTrue(Integer v) { return v != null && v == 1; }

    @Override
    @Transactional
    public void cancel(Integer taskId, Integer applicantId) {
        Application app = applicationMapper.selectByTaskAndApplicant(taskId, applicantId);
        if (app == null) throw new BusinessException(404, "不存在此申请");
        if (app.getStatus() != 0) throw new BusinessException("该申请已处理，无法撤销");
        applicationMapper.deleteById(app.getId());
    }
}
