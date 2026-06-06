package com.komima.service.impl;

import com.komima.dto.TaskDTO;
import com.komima.entity.Task;
import com.komima.enums.TaskStatus;
import com.komima.exception.BusinessException;
import com.komima.mapper.TaskMapper;
import com.komima.mapper.UserMapper;
import com.komima.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务服务实现类
 * 负责处理委托相关的业务逻辑，包括发布委托、查询委托、删除委托等功能
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 发布委托
     * 用户发布新委托时调用此方法，会：
     * - 验证用户是否存在
     * - 设置默认招募人数（默认为1）
     * - 设置初始状态为待接单
     * - 记录创建时间和更新时间
     *
     * @param dto 委托数据传输对象，包含委托标题、内容、发布者ID、分类、是否有报酬、招募人数等信息
     * @return 创建的委托记录
     * @throws BusinessException 当用户不存在时抛出业务异常
     */
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

    /**
     * 获取所有委托列表
     *
     * @return 委托记录列表
     */
    @Override
    public List<Task> listAll() { return taskMapper.selectAll(); }

    /**
     * 根据条件筛选委托列表
     *
     * @param status 委托状态（可选）
     * @param category 委托分类（可选）
     * @param hasReward 是否有报酬（可选）
     * @param excludeUserId 排除的用户ID（可选，用于排除用户自己的委托）
     * @return 筛选后的委托记录列表
     */
    @Override
    public List<Task> listFiltered(Integer status, Integer category, Integer hasReward, Integer excludeUserId) {
        return taskMapper.selectFiltered(status, category, hasReward, excludeUserId);
    }

    /**
     * 获取指定用户发布的所有委托列表
     *
     * @param userId 用户ID
     * @return 委托记录列表
     */
    @Override
    public List<Task> listByUserId(Integer userId) { return taskMapper.selectByUserId(userId); }

    /**
     * 获取指定用户申请的所有委托列表
     *
     * @param applicantId 申请人ID
     * @return 委托记录列表
     */
    @Override
    public List<Task> listByApplicantId(Integer applicantId) { return taskMapper.selectByApplicantId(applicantId); }

    /**
     * 根据ID获取委托详情
     *
     * @param id 委托ID
     * @return 委托记录
     * @throws BusinessException 当委托不存在时抛出业务异常
     */
    @Override
    public Task getById(Integer id) {
        Task task = taskMapper.selectById(id);
        if (task == null) throw new BusinessException(404, "委托不存在");
        return task;
    }

    /**
     * 删除委托（管理员操作）
     *
     * @param taskId 委托ID
     * @throws BusinessException 当委托不存在时抛出业务异常
     */
    @Override
    @Transactional
    public void deleteTask(Integer taskId) {
        getById(taskId);
        taskMapper.deleteById(taskId);
    }

    /**
     * 删除自己的待接单委托
     * 用户只能删除自己发布的、状态为待接单的委托
     *
     * @param taskId 委托ID
     * @param userId 用户ID
     * @throws BusinessException 当验证失败时抛出业务异常
     */
    @Override
    @Transactional
    public void deleteMyPendingTask(Integer taskId, Integer userId) {
        Task task = getById(taskId);
        if (!task.getUserId().equals(userId)) throw new BusinessException(403, "只能删除自己的委托");
        if (task.getStatus() != 0) throw new BusinessException("只能删除待接单状态的委托");
        taskMapper.deleteById(taskId);
    }

    /**
     * 过期委托处理
     * 将超过指定天数的待接单委托标记为已过期
     *
     * @param days 天数阈值
     */
    @Override
    @Transactional
    public void expireTasks(int days) {
        taskMapper.expirePendingTasks(days, TaskStatus.EXPIRED.getCode());
    }

    /**
     * 定时过期任务
     * 每天凌晨3点自动执行，将30天前的待接单委托标记为已过期
     * 使用Spring的@Scheduled注解实现定时任务
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void scheduledTaskExpiration() {
        expireTasks(30);
    }
}
