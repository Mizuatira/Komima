package com.komima.service;

import com.komima.dto.EvaluateDTO;
import com.komima.entity.Evaluate;

import java.util.List;

/**
 * 评价服务接口
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
public interface EvaluateService {

    Evaluate create(EvaluateDTO dto);

    List<Evaluate> getByTaskId(Integer taskId);

    List<Evaluate> getByUserId(Integer userId);
}
