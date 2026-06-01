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

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    private EvaluateMapper evaluateMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

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

    @Override
    public List<Evaluate> getByTaskId(Integer taskId) { return evaluateMapper.selectByTaskId(taskId); }

    @Override
    public List<Evaluate> getByUserId(Integer userId) { return evaluateMapper.selectByUserId(userId); }
}
