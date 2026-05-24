package com.komima.service.impl;

import com.komima.dto.EvaluateDTO;
import com.komima.entity.Evaluate;
import com.komima.entity.Task;
import com.komima.enums.TaskStatus;
import com.komima.exception.BusinessException;
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

    @Override
    @Transactional
    public Evaluate create(EvaluateDTO evaluateDTO) {
        Task task = taskMapper.selectById(evaluateDTO.getTaskId());
        if (task == null) {
            throw new BusinessException(404, "委托不存在");
        }
        if (task.getStatus() != TaskStatus.COMPLETED.getCode()) {
            throw new BusinessException("仅已完成状态的委托可以评价");
        }
        Evaluate evaluate = new Evaluate();
        evaluate.setTaskId(evaluateDTO.getTaskId());
        evaluate.setUserId(evaluateDTO.getUserId());
        evaluate.setScore(evaluateDTO.getScore());
        evaluate.setContent(evaluateDTO.getContent());
        evaluate.setCreateTime(LocalDateTime.now());
        evaluateMapper.insert(evaluate);
        return evaluate;
    }

    @Override
    public List<Evaluate> getByTaskId(Integer taskId) {
        return evaluateMapper.selectByTaskId(taskId);
    }

    @Override
    public List<Evaluate> getByUserId(Integer userId) {
        return evaluateMapper.selectByUserId(userId);
    }
}
