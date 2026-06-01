package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.entity.Application;
import com.komima.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    public ApiResponse<Application> apply(@RequestParam Integer taskId, @RequestParam Integer userId) {
        return ApiResponse.success("已申请", applicationService.apply(taskId, userId));
    }

    @GetMapping("/list/{taskId}")
    public ApiResponse<List<Application>> listByTask(@PathVariable Integer taskId) {
        return ApiResponse.success(applicationService.listByTask(taskId));
    }

    @PostMapping("/approve")
    public ApiResponse<Void> approve(@RequestParam Integer taskId,
                                     @RequestParam Integer applicantId,
                                     @RequestParam Integer publisherId) {
        applicationService.approve(taskId, applicantId, publisherId);
        return ApiResponse.success("已选择", null);
    }

    @PostMapping("/reject")
    public ApiResponse<Void> reject(@RequestParam Integer taskId,
                                    @RequestParam Integer applicantId,
                                    @RequestParam Integer publisherId) {
        applicationService.reject(taskId, applicantId, publisherId);
        return ApiResponse.success("已拒绝", null);
    }

    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirmComplete(@PathVariable Integer id, @RequestParam Integer userId) {
        applicationService.confirmComplete(id, userId);
        return ApiResponse.success("确认成功", null);
    }
}
