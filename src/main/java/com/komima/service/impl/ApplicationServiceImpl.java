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

/**
 * 申请服务实现类
 * 负责处理委托申请的相关业务逻辑，包括申请委托、审批申请、确认完成等功能
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 申请委托
     * 用户申请某个委托时调用此方法，会进行多项验证：
     * - 委托是否存在
     * - 是否申请了自己的委托
     * - 委托状态是否允许申请
     * - 招募人数是否已满
     * - 是否已经申请过
     *
     * @param taskId 委托ID
     * @param applicantId 申请人ID
     * @return 创建的申请记录
     * @throws BusinessException 当验证失败时抛出业务异常
     */
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

    /**
     * 获取指定委托的所有申请列表
     *
     * @param taskId 委托ID
     * @return 申请记录列表
     */
    @Override
    public List<Application> listByTask(Integer taskId) {
        return applicationMapper.selectByTaskId(taskId);
    }

    /**
     * 批准申请
     * 委托发布者批准某个申请者的申请，会：
     * - 验证委托是否存在
     * - 验证操作人是否为发布者
     * - 检查招募人数是否已满
     * - 更新申请状态为已批准
     * - 若为第一个批准的申请，将委托状态更新为进行中
     *
     * @param taskId 委托ID
     * @param applicantId 申请人ID
     * @param publisherId 发布者ID（操作人）
     * @throws BusinessException 当验证失败时抛出业务异常
     */
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

    /**
     * 拒绝申请
     * 委托发布者拒绝某个申请者的申请
     *
     * @param taskId 委托ID
     * @param applicantId 申请人ID
     * @param publisherId 发布者ID（操作人）
     * @throws BusinessException 当验证失败时抛出业务异常
     */
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

    /**
     * 确认委托完成
     * 发布者或申请者确认委托完成：
     * - 验证申请是否存在且已批准
     * - 验证操作人是否有权限
     * - 更新对应的确认状态
     * - 当所有参与者都确认后，将委托状态更新为已完成
     *
     * @param applicationId 申请ID
     * @param userId 操作人ID
     * @throws BusinessException 当验证失败时抛出业务异常
     */
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

    /**
     * 判断整数是否为真值（1）
     *
     * @param v 待判断的整数
     * @return true表示为真，false表示为假或null
     */
    private boolean isTrue(Integer v) { return v != null && v == 1; }

    /**
     * 撤销申请
     * 申请人在申请被处理前可以撤销自己的申请
     *
     * @param taskId 委托ID
     * @param applicantId 申请人ID
     * @throws BusinessException 当验证失败时抛出业务异常
     */
    @Override
    @Transactional
    public void cancel(Integer taskId, Integer applicantId) {
        Application app = applicationMapper.selectByTaskAndApplicant(taskId, applicantId);
        if (app == null) throw new BusinessException(404, "不存在此申请");
        if (app.getStatus() != 0) throw new BusinessException("该申请已处理，无法撤销");
        applicationMapper.deleteById(app.getId());
    }
}
