package com.komima.service;

import com.komima.entity.Application;

import java.util.List;

public interface ApplicationService {

    Application apply(Integer taskId, Integer applicantId);

    List<Application> listByTask(Integer taskId);

    void approve(Integer taskId, Integer applicantId, Integer publisherId);

    void reject(Integer taskId, Integer applicantId, Integer publisherId);

    void confirmComplete(Integer applicationId, Integer userId);

    void cancel(Integer taskId, Integer applicantId);
}
