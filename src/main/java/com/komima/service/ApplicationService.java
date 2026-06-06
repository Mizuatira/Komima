package com.komima.service;

import com.komima.entity.Application;

import java.util.List;

/**
 * 申请服务接口
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
public interface ApplicationService {

    Application apply(Integer taskId, Integer applicantId);

    List<Application> listByTask(Integer taskId);

    void approve(Integer taskId, Integer applicantId, Integer publisherId);

    void reject(Integer taskId, Integer applicantId, Integer publisherId);

    void confirmComplete(Integer applicationId, Integer userId);

    void cancel(Integer taskId, Integer applicantId);
}
