package com.komima.service.impl;

import com.komima.dto.EvaluateDTO;
import com.komima.entity.Application;
import com.komima.entity.Evaluate;
import com.komima.entity.Task;
import com.komima.enums.TaskStatus;
import com.komima.exception.BusinessException;
import com.komima.mapper.ApplicationMapper;
import com.komima.mapper.EvaluateMapper;
import com.komima.mapper.TaskMapper;
import com.komima.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评价服务实现类
 * 负责处理委托完成后的评价相关业务逻辑
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    private EvaluateMapper evaluateMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    /**
     * 创建评价
     * 委托完成后，参与者可以相互评价，会进行以下验证：
     * - 委托是否存在且已完成
     * - 评价人是否为委托参与者（发布者或已批准的申请者）
     * - 是否评价了自己
     * - 是否已经评价过
     *
     * @param dto 评价数据传输对象，包含委托ID、评价人ID、被评价人ID、评分和评价内容
     * @return 创建的评价记录
     * @throws BusinessException 当验证失败时抛出业务异常
     */
    @Override
    @Transactional
    public Evaluate create(EvaluateDTO dto) {
        Task task = taskMapper.selectById(dto.getTaskId());
        if (task == null) throw new BusinessException(404, "委托不存在");
        if (task.getStatus() != TaskStatus.COMPLETED.getCode()) throw new BusinessException("仅已完成委托可评价");

        int from = dto.getFromUserId();
        int pub = task.getUserId();
        Application app = applicationMapper.selectByTaskAndApplicant(dto.getTaskId(), from);
        boolean isApproved = app != null && app.getStatus() == 1;
        if (from != pub && !isApproved) throw new BusinessException(403, "非参与者无法评价");
        if (from == dto.getToUserId()) throw new BusinessException("不能评价自己");
        if (evaluateMapper.selectByTaskAndFromUser(dto.getTaskId(), from) != null) {
            throw new BusinessException("你已评价过");
        }

        evaluateMapper.insert(new Evaluate()
                .setTaskId(dto.getTaskId()).setFromUserId(from).setToUserId(dto.getToUserId())
                .setScore(dto.getScore()).setContent(dto.getContent())
                .setCreateTime(LocalDateTime.now()));
        return evaluateMapper.selectByTaskAndFromUser(dto.getTaskId(), from);
    }

    /**
     * 获取指定委托的所有评价列表
     *
     * @param taskId 委托ID
     * @return 评价记录列表
     */
    @Override
    public List<Evaluate> getByTaskId(Integer taskId) { return evaluateMapper.selectByTaskId(taskId); }

    /**
     * 获取指定用户收到的所有评价列表
     *
     * @param userId 用户ID
     * @return 评价记录列表
     */
    @Override
    public List<Evaluate> getByUserId(Integer userId) { return evaluateMapper.selectByUserId(userId); }
}
