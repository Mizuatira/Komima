package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.entity.Application;
import com.komima.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 申请控制器
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@RestController
@RequestMapping("/api/application")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * 申请任务
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 申请信息
     */
    @PostMapping("/apply")
    public ApiResponse<Application> apply(@RequestParam Integer taskId, @RequestParam Integer userId) {
        return ApiResponse.success("已申请", applicationService.apply(taskId, userId));
    }

    /**
     * 获取任务的申请列表
     * @param taskId 任务ID
     * @return 申请列表
     */
    @GetMapping("/list/{taskId}")
    public ApiResponse<List<Application>> listByTask(@PathVariable Integer taskId) {
        return ApiResponse.success(applicationService.listByTask(taskId));
    }

    /**
     * 批准申请
     * @param taskId 任务ID
     * @param applicantId 申请人ID
     * @param publisherId 发布人ID
     * @return 响应结果
     */
    @PostMapping("/approve")
    public ApiResponse<Void> approve(@RequestParam Integer taskId,
                                     @RequestParam Integer applicantId,
                                     @RequestParam Integer publisherId) {
        applicationService.approve(taskId, applicantId, publisherId);
        return ApiResponse.success("已选择", null);
    }

    /**
     * 拒绝申请
     * @param taskId 任务ID
     * @param applicantId 申请人ID
     * @param publisherId 发布人ID
     * @return 响应结果
     */
    @PostMapping("/reject")
    public ApiResponse<Void> reject(@RequestParam Integer taskId,
                                    @RequestParam Integer applicantId,
                                    @RequestParam Integer publisherId) {
        applicationService.reject(taskId, applicantId, publisherId);
        return ApiResponse.success("已拒绝", null);
    }

    /**
     * 确认任务完成
     * @param id 申请ID
     * @param userId 用户ID
     * @return 响应结果
     */
    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirmComplete(@PathVariable Integer id, @RequestParam Integer userId) {
        applicationService.confirmComplete(id, userId);
        return ApiResponse.success("确认成功", null);
    }

    /**
     * 撤销申请
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 响应结果
     */
    @PostMapping("/cancel")
    public ApiResponse<Void> cancel(@RequestParam Integer taskId, @RequestParam Integer userId) {
        applicationService.cancel(taskId, userId);
        return ApiResponse.success("已撤销", null);
    }
}
