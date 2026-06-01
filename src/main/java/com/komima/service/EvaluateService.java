package com.komima.service;

import com.komima.dto.EvaluateDTO;
import com.komima.entity.Evaluate;

import java.util.List;

public interface EvaluateService {

    Evaluate create(EvaluateDTO dto);

    List<Evaluate> getByTaskId(Integer taskId);

    List<Evaluate> getByUserId(Integer userId);
}
